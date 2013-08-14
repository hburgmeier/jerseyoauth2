package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ErrorCode;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
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
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.authorize.InvalidUserException;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.api.IResponseBuilder;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpRequestAdapter;

public class AuthorizationService implements IAuthorizationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationService.class);
	
	private final IClientService clientService;
	private final IUserService userService;
	private final IAuthorizationFlow authFlow;
	private final ITokenService tokenService;
	private final IRequestFactory requestFactory;
	private final IConfiguration configuration;
	private final IResponseBuilder responseBuilder;
	private final ScopeValidator scopeValidator;
	
	private final Set<String> defaultScopes;
	
	@Inject
	public AuthorizationService(final IClientService clientService, final IUserService userService,
			final IAuthorizationFlow authFlow, final IConfiguration configuration, final ITokenService tokenService,
			final IRequestFactory requestFactory, final IResponseBuilder responseBuilder)
	{
		this.clientService = clientService;
		this.userService = userService;
		this.authFlow = authFlow;
		this.configuration = configuration;
		this.tokenService = tokenService;
		this.requestFactory = requestFactory;
		this.responseBuilder = responseBuilder;
		Set<String> defScopes = configuration.getDefaultScopes();
		this.defaultScopes = defScopes==null?Collections.<String>emptySet():defScopes;
		this.scopeValidator = new ScopeValidator(configuration);
	}	
	
	@Override
	public void evaluateAuthorizationRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws AuthorizationFlowException, IOException, ServletException, ResponseBuilderException {
		IRegisteredClientApp regClientApp = null;
		try {
			IAuthorizationRequest oauthRequest = requestFactory.parseAuthorizationRequest(new HttpRequestAdapter(request), 
					configuration.getEnableAuthorizationHeaderForClientAuth());
			LOGGER.debug("Parsing of AuthzRequest successful");

			IUser user = userService.getCurrentUser(request);
			if (user == null) {
				throw new InvalidUserException();
			}

			regClientApp = clientService.getRegisteredClient(oauthRequest.getClientId());
			if (regClientApp == null) {
				throw new OAuth2ProtocolException(OAuth2ErrorCode.UNAUTHORIZED_CLIENT, "client " + oauthRequest.getClientId()
						+ " is invalid", oauthRequest.getState());
			}

			Set<String> scopes = oauthRequest.getScopes();
			if (scopes==null || scopes.isEmpty()) {
				LOGGER.warn("using default scopes");
				scopes = defaultScopes;
			}
			
			try {
				scopeValidator.validateScopes(scopes);
			} catch (InvalidScopeException e)
			{
				LOGGER.error("Scope {} is unknown", e.getScope());
				throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_SCOPE, oauthRequest.getState(), "Scope is invalid", e);
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
				sendAuthorizationReponse(request, response, reqResponseType, regClientApp, authorizedClientApp, oauthRequest.getState());
			} else {
				LOGGER.debug("client is not authorized or missing scopes {}", scopes);
				authFlow.startAuthorizationFlow(user, regClientApp, scopes, reqResponseType, request, response, servletContext);
			}
		} catch (InvalidUserException e) {
			LOGGER.error("Missing or invalid user");
			authFlow.handleMissingUser(request, response, servletContext);
		} catch (OAuth2ParseException e) {
			LOGGER.error("Problem with OAuth2 protocol", e);
			sendErrorResponse(e, response, regClientApp == null ? null : regClientApp.getCallbackUrl());
		} catch (OAuth2ProtocolException e) {
			LOGGER.error("Problem with OAuth2 protocol", e);
			sendErrorResponse(e, response, regClientApp == null ? null : regClientApp.getCallbackUrl());
		}
	}

	@Override
	public void sendAuthorizationReponse(HttpServletRequest request, HttpServletResponse response,
			ResponseType reqResponseType, IRegisteredClientApp regClientApp, IAuthorizedClientApp authorizedClientApp, 
			String state)
			throws OAuth2ProtocolException, IOException, ResponseBuilderException {
		try {
			if (reqResponseType.equals(ResponseType.CODE)) {
				IPendingClientToken pendingClientToken = clientService
						.createPendingClientToken(authorizedClientApp);
				sendAuthorizationReponse(request, response, pendingClientToken, regClientApp, state);
			} else {
				LOGGER.debug("issue new token for token response type");
				tokenService.issueNewToken(request, response, authorizedClientApp, reqResponseType, state);
			}
		} catch (ClientServiceException e) {
			throw new OAuth2ProtocolException(OAuth2ErrorCode.SERVER_ERROR, "client is invalid", state, e);
		}
	}
	
	protected void sendAuthorizationReponse(HttpServletRequest request, HttpServletResponse response, 
			IPendingClientToken clientAuth, IRegisteredClientApp clientApp, String state) throws ResponseBuilderException {
		try {
			URI redirectUrl = new URI(clientApp.getCallbackUrl());
			responseBuilder.buildAuthorizationCodeResponse(clientAuth.getCode(), redirectUrl , state, response);
		} catch (URISyntaxException e) {
			throw new ResponseBuilderException(e);
		}
	}
	
	protected void sendErrorResponse(OAuth2ProtocolException ex,
			HttpServletResponse response, String redirectUrl) throws ResponseBuilderException {
		try {
			URI redirectUri = new URI(redirectUrl);
			responseBuilder.buildAuthorizationRequestErrorResponse(ex, redirectUri, response);
		} catch (URISyntaxException e) {
			throw new ResponseBuilderException(e);
		}
	}	

	protected void validateCodeRequest(IAuthorizationRequest oauthRequest, IRegisteredClientApp regClientApp) throws OAuth2ProtocolException
	{
		if (oauthRequest.getClientSecret() != null) {
			if (!regClientApp.getClientSecret().equals(oauthRequest.getClientSecret())) {
				throw new OAuth2ProtocolException(OAuth2ErrorCode.UNAUTHORIZED_CLIENT, "client is invalid", oauthRequest.getState());
			}
		}
	}
	
	protected void validateTokenRequest(IAuthorizationRequest oauthRequest, IRegisteredClientApp regClientApp) throws OAuth2ProtocolException
	{
		if (regClientApp.getClientType().equals(ClientType.CONFIDENTIAL)) {
			throw new OAuth2ProtocolException(OAuth2ErrorCode.UNSUPPORTED_RESPONSE_TYPE, "client type is invalid", oauthRequest.getState());
		}
		if (!oauthRequest.getRedirectURI().equals(regClientApp.getCallbackUrl())) {
			throw new OAuth2ProtocolException(OAuth2ErrorCode.UNAUTHORIZED_CLIENT, "redirect uri does not match", oauthRequest.getState());
		}
	}
	
}
