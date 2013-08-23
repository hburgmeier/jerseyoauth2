package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.EnumSet;
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
import com.github.hburgmeier.jerseyoauth2.authsrv.api.authorization.IAuthorizationService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientType;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IPendingClientToken;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.authorize.InvalidUserException;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.ClientIdentityValidator;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.InvalidScopeException;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.ScopeValidator;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.api.IResponseBuilder;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpHeaders;
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
	private final ClientIdentityValidator clientIdValidator = new ClientIdentityValidator();
	
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
		try {
			IRegisteredClientApp regClientApp = null;
			IAuthorizationRequest oauthRequest = null;
			try {
				oauthRequest = requestFactory.parseAuthorizationRequest(new HttpRequestAdapter(request), 
						configuration.getEnableAuthorizationHeaderForClientAuth());
				LOGGER.debug("Parsing of AuthzRequest successful");

				IUser user = userService.getCurrentUser(request);
				if (user == null) {
					throw new InvalidUserException();
				}

				regClientApp = clientService.getRegisteredClient(oauthRequest.getClientId());
				if (regClientApp == null) {
					throw new InvalidClientException();
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
					authFlow.startAuthorizationFlow(user, regClientApp, scopes, oauthRequest, request, response, servletContext);
				}
			} catch (InvalidUserException e) {
				LOGGER.error("Missing or invalid user");
				authFlow.handleMissingUser(request, response, servletContext);
			} catch (OAuth2ParseException e) {
				LOGGER.error("Problem with OAuth2 protocol", e);
				URI redirectUrl = getRedirectUri(regClientApp, oauthRequest, true);
				sendErrorResponse(e, response, redirectUrl);
			} catch (OAuth2ProtocolException e) {
				LOGGER.error("Problem with OAuth2 protocol", e);
				if (e.getErrorCode() == OAuth2ErrorCode.INVALID_CLIENT &&
					oauthRequest.hasUsedAuhorizationHeader())
				{
					sendUnauthorizedResponse(response);
				} else {
					URI redirectUrl = getRedirectUri(regClientApp, oauthRequest, true);
					sendErrorResponse(e, response, redirectUrl);
				}
			}
		} catch (InvalidClientException e) {
			LOGGER.error("Problem with the redirect Url", e);
			authFlow.handleInvalidClient(request, response, servletContext);
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
	
	@Override
	public void sendAuthorizationReponse(HttpServletRequest request, HttpServletResponse response,
			IAuthorizationRequest originalRequest, IRegisteredClientApp regClientApp,
			IAuthorizedClientApp authorizedClientApp) throws IOException, OAuth2ProtocolException,
			ResponseBuilderException {
		try {
			if (originalRequest.getResponseType() == ResponseType.CODE) {
				IPendingClientToken pendingClientToken = clientService
						.createPendingClientToken(authorizedClientApp);
				sendAuthorizationReponse(request, response, pendingClientToken, regClientApp, originalRequest.getState());
			} else {
				LOGGER.debug("issue new token for token response type");
				tokenService.issueNewToken(request, response, authorizedClientApp, originalRequest.getResponseType(), originalRequest.getState());
			}
		} catch (ClientServiceException e) {
			throw new OAuth2ProtocolException(OAuth2ErrorCode.SERVER_ERROR, "client is invalid", originalRequest.getState(), e);
		}
	}	

	@Override
	public void sendErrorResponse(OAuth2ProtocolException ex,
			HttpServletResponse response, URI redirectUrl) throws ResponseBuilderException {
		responseBuilder.buildAuthorizationRequestErrorResponse(ex, redirectUrl, response);
	}
	
	protected void sendUnauthorizedResponse(HttpServletResponse response)
	{
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader(HttpHeaders.AUTHENTICATE, "Basic");
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

	protected URI getRedirectUri(IRegisteredClientApp regClientApp, IAuthorizationRequest oauthRequest, boolean error) throws InvalidClientException
	{
		String result = null;
		if (oauthRequest!=null && oauthRequest.getRedirectURI()!=null)
		{
			result = oauthRequest.getRedirectURI();
		}
		if (regClientApp!=null)
		{
			if (result!=null && regClientApp.getCallbackUrl()!=null)
			{
				if (!error) {
					throw new InvalidClientException();
				}
				else {
					result = regClientApp.getCallbackUrl();
				}
			}
			if (result == null) {
				result = regClientApp.getCallbackUrl();
			}
		}
		if (result == null)
		{
			throw new InvalidClientException();
		} else
		{
			try {
				return new URI(result);
			} catch (URISyntaxException e) {
				throw new InvalidClientException(e);
			}
		}
	}
	
	protected void validateCodeRequest(IAuthorizationRequest oauthRequest, IRegisteredClientApp regClientApp) throws OAuth2ProtocolException
	{
		clientIdValidator.validateAuthorizationRequest(oauthRequest, regClientApp);
	}
	
	protected void validateTokenRequest(IAuthorizationRequest oauthRequest, IRegisteredClientApp regClientApp) throws OAuth2ProtocolException
	{
		EnumSet<ClientType> allowedClientTypes = configuration.getAllowedClientTypesForImplicitGrant();
		if (!allowedClientTypes.contains(regClientApp.getClientType())) {
			throw new OAuth2ProtocolException(OAuth2ErrorCode.UNSUPPORTED_RESPONSE_TYPE, "Client type is allowed for Implicit Grant.", oauthRequest.getState());
		}
		if (!oauthRequest.getRedirectURI().equals(regClientApp.getCallbackUrl())) {
			throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_CLIENT, "Redirect uri does not match", oauthRequest.getState());
		}
		clientIdValidator.validateAuthorizationRequest(oauthRequest, regClientApp);
	}
	
}
