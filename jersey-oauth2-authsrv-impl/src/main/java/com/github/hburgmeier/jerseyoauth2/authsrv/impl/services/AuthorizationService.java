package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.error.OAuthError.TokenResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2Exception;
import com.github.hburgmeier.jerseyoauth2.api.types.ResponseType;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientType;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizationService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IPendingClientToken;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.amber.OAuth2AuthzRequest;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.authorize.InvalidUserException;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpRequestAdapter;
import com.google.inject.Inject;

public class AuthorizationService implements IAuthorizationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationService.class);
	
	private final IClientService clientService;
	private final IUserService userService;
	private final IAuthorizationFlow authFlow;
	private final ITokenService tokenService;
	private final IRequestFactory requestFactory;
	private final Set<String> defaultScopes;
	private final IConfiguration configuration;
	
	@Inject
	public AuthorizationService(final IClientService clientService, final IUserService userService,
			final IAuthorizationFlow authFlow, final IConfiguration configuration, final ITokenService tokenService,
			final IRequestFactory requestFactory)
	{
		this.clientService = clientService;
		this.userService = userService;
		this.authFlow = authFlow;
		this.configuration = configuration;
		this.tokenService = tokenService;
		this.requestFactory = requestFactory;
		Set<String> defScopes = configuration.getDefaultScopes();
		this.defaultScopes = defScopes==null?Collections.<String>emptySet():defScopes;
	}	
	
	@Override
	public void evaluateAuthorizationRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws AuthorizationFlowException, OAuthSystemException, IOException, ServletException {
		IRegisteredClientApp regClientApp = null;
		try {
//			OAuth2AuthzRequest oauthRequest = new OAuth2AuthzRequest(request,
//					configuration.getEnableAuthorizationHeaderForClientAuth());
			IAuthorizationRequest oauthRequest = requestFactory.parseAuthorizationRequest(new HttpRequestAdapter(request), 
					configuration.getEnableAuthorizationHeaderForClientAuth());
			LOGGER.debug("Parsing of AuthzRequest successful");

			IUser user = userService.getCurrentUser(request);
			if (user == null)
				throw new InvalidUserException();

			regClientApp = clientService.getRegisteredClient(oauthRequest.getClientId());
			if (regClientApp == null)
				throw OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client " + oauthRequest.getClientId()
						+ " is invalid");

			Set<String> scopes = oauthRequest.getScopes();
			if (scopes.isEmpty()) {
				LOGGER.warn("using default scopes");
				scopes = defaultScopes;
			}

			LOGGER.debug("Response Type {}", oauthRequest.getResponseType());
			ResponseType reqResponseType = oauthRequest.getResponseType();

			switch (reqResponseType)
			{
			case CODE:
				validateCodeRequest(oauthRequest, regClientApp);
				break;
			case TOKEN:
				validateTokenRequest(oauthRequest, regClientApp);
				break;
			}
			
			IAuthorizedClientApp authorizedClientApp = clientService.isAuthorized(user, regClientApp.getClientId(),
					scopes);
			if (authorizedClientApp != null) {
				LOGGER.debug("client is already authorized");
				sendAuthorizationReponse(request, response, reqResponseType, regClientApp, authorizedClientApp);
			} else {
				LOGGER.debug("client is not authorized or missing scopes {}", scopes);
				authFlow.startAuthorizationFlow(user, regClientApp, scopes, reqResponseType, request, response, servletContext);
			}
		} catch (OAuthProblemException e) {
			LOGGER.error("Problem with OAuth2 protocol", e);
			sendErrorResponse(e, response, regClientApp == null ? null : regClientApp.getCallbackUrl());
		} catch (InvalidUserException e) {
			LOGGER.error("Missing or invalid user");
			authFlow.handleMissingUser(request, response, servletContext);
		} catch (OAuth2Exception e) {
			LOGGER.error("Problem with OAuth2 protocol", e);
//			sendErrorResponse(e, response, regClientApp == null ? null : regClientApp.getCallbackUrl());
		}
	}

	@Override
	public void sendAuthorizationReponse(HttpServletRequest request, HttpServletResponse response,
			ResponseType reqResponseType, IRegisteredClientApp regClientApp, IAuthorizedClientApp authorizedClientApp)
			throws OAuthSystemException, IOException, OAuthProblemException {
		try {
			if (reqResponseType.equals(ResponseType.CODE)) {
				IPendingClientToken pendingClientToken = clientService
						.createPendingClientToken(authorizedClientApp);
				sendAuthorizationReponse(request, response, pendingClientToken, regClientApp);
			} else {
				LOGGER.debug("issue new token for token response type");
				tokenService.issueNewToken(request, response, authorizedClientApp, reqResponseType);
			}
		} catch (ClientServiceException e) {
			throw OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client is invalid");
		}
	}
	
	@Override
	public void sendAuthorizationReponse(HttpServletRequest request, HttpServletResponse response, 
			IPendingClientToken clientAuth, IRegisteredClientApp clientApp) throws OAuthSystemException, IOException {
		OAuthResponse resp = OAuthASResponse
		        .authorizationResponse(request, HttpServletResponse.SC_FOUND)
		        .setCode(clientAuth.getCode())                    
		        .location(clientApp.getCallbackUrl())
		        .buildQueryMessage();

		response.sendRedirect(resp.getLocationUri());
	}
	
	@Override
	public void sendErrorResponse(OAuthProblemException ex,
			HttpServletResponse response, String redirectUri) throws OAuthSystemException, IOException {
        OAuthErrorResponseBuilder responseBuilder = OAuthASResponse
				        .errorResponse(HttpServletResponse.SC_FOUND)
				        .error(ex);
        if (redirectUri!=null)
        	responseBuilder = responseBuilder.location(redirectUri);
		final OAuthResponse resp = responseBuilder.buildQueryMessage();
                   
        response.sendRedirect(resp.getLocationUri());
	}

	protected void validateCodeRequest(IAuthorizationRequest oauthRequest, IRegisteredClientApp regClientApp) throws OAuthProblemException
	{
		if (oauthRequest.getClientSecret() != null) {
			if (!regClientApp.getClientSecret().equals(oauthRequest.getClientSecret()))
				throw OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client is invalid");
		}
	}
	
	protected void validateTokenRequest(IAuthorizationRequest oauthRequest, IRegisteredClientApp regClientApp) throws OAuthProblemException
	{
		if (regClientApp.getClientType().equals(ClientType.CONFIDENTIAL))
			throw OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client type is invalid");
		if (!oauthRequest.getRedirectURI().equals(regClientApp.getCallbackUrl()))
			throw OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "redirect uri does not match");
	}
	
}
