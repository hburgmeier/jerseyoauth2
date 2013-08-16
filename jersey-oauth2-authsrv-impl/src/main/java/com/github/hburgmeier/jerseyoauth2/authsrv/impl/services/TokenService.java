package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthCodeAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRefreshTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ErrorCode;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.github.hburgmeier.jerseyoauth2.api.types.GrantType;
import com.github.hburgmeier.jerseyoauth2.api.types.ResponseType;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IPendingClientToken;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.TokenGenerationException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.TokenStorageException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.api.IResponseBuilder;

public class TokenService implements ITokenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);
	
	private final IAccessTokenStorageService accessTokenService;
	private final IClientService clientService;
	private final ITokenGenerator tokenGenerator;
	private final IResponseBuilder responseBuilder;
	private final IAuthorizationFlow authFlow;
	
	protected final boolean issueRefreshToken;
	protected final boolean allowScopeEnhancement;
	protected final ScopeValidator scopeValidator;
	
	@Inject
	public TokenService(final IAccessTokenStorageService accessTokenService, final IClientService clientService, final ITokenGenerator tokenGenerator,
			final IAuthorizationFlow authFlow, final IConfiguration configuration, final IResponseBuilder responseBuilder)
	{
		this.accessTokenService = accessTokenService;
		this.clientService = clientService;
		this.tokenGenerator = tokenGenerator;
		this.authFlow = authFlow;
		this.responseBuilder = responseBuilder;
		
		this.issueRefreshToken = configuration.getEnableRefreshTokenGeneration();
		this.allowScopeEnhancement = configuration.getAllowScopeEnhancementWithRefreshToken();
		this.scopeValidator = new ScopeValidator(configuration);
		
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, IAccessTokenRequest oauthRequest)
			throws OAuth2ProtocolException, ResponseBuilderException, AuthorizationFlowException {
		LOGGER.debug("Token request received, grant type {}", oauthRequest.getGrantType());
		
		if (oauthRequest.getGrantType() == GrantType.REFRESH_TOKEN) {

			if (issueRefreshToken) {
				refreshToken(request, response, servletContext, (IRefreshTokenRequest)oauthRequest);
			}
			else {
				LOGGER.error("Refresh token generation is disabled");
				throw new OAuth2ProtocolException(OAuth2ErrorCode.UNSUPPORTED_GRANT_TYPE, "Refresh token is disabled", null);
			}

		} else if (oauthRequest.getGrantType() == GrantType.AUTHORIZATION_REQUEST) {

			handleAuthorizationRequest(request, response, oauthRequest);
		}
	}

	@Override
	public void issueNewToken(HttpServletRequest request, HttpServletResponse response, IAuthorizedClientApp clientApp, ResponseType responseType, String state)
			throws OAuth2ProtocolException, ResponseBuilderException {

		try {
		
			String accessToken = tokenGenerator.createAccessToken();
			String refreshToken = issueRefreshToken?tokenGenerator.createRefreshToken():null;
		
			IAccessTokenInfo accessTokenInfo = accessTokenService.issueToken(accessToken, refreshToken,
					clientApp);
			LOGGER.debug("token {} issued", accessToken);

			sendTokenResponse(request, response, accessTokenInfo, responseType, state);
		} catch (TokenStorageException e) {
			LOGGER.error("error with token storage", e);
			throw new OAuth2ProtocolException(OAuth2ErrorCode.SERVER_ERROR, "Server error", null, e);
		} catch (TokenGenerationException e) {
			LOGGER.error("error with token generation", e);
			throw new OAuth2ProtocolException(OAuth2ErrorCode.SERVER_ERROR, "Server error", null, e);
		}
	}	

	@Override
	public void sendErrorResponse(HttpServletResponse response, OAuth2ProtocolException ex) throws ResponseBuilderException {
		responseBuilder.buildRequestTokenErrorResponse(ex, response);
	}

	@Override
	public void sendTokenResponse(HttpServletRequest request, HttpServletResponse response, IAccessTokenInfo accessTokenInfo, ResponseType responseType, String state)
			throws ResponseBuilderException {
		LOGGER.debug("sending response for {}", responseType);
		if (responseType == ResponseType.TOKEN)
		{
			try {
				responseBuilder.buildImplicitGrantAccessTokenResponse(accessTokenInfo, new URI(accessTokenInfo.getClientApp().getCallbackUrl()), state, response);
			} catch (URISyntaxException e) {
				LOGGER.debug("error with callback URL {}", accessTokenInfo.getClientApp().getCallbackUrl());
				throw new ResponseBuilderException(e);
			}
		} else {
			responseBuilder.buildAccessTokenResponse(accessTokenInfo, state, response);
		}
	}
	
	@Override
	public void removeTokensForUser(IUser user) {
		accessTokenService.invalidateTokensForUser(user);
		clientService.removePendingTokensForUser(user);
	}
	
	protected void handleAuthorizationRequest(HttpServletRequest request, HttpServletResponse response,
			IAccessTokenRequest oauthRequest) throws OAuth2ProtocolException, ResponseBuilderException {
		IAuthCodeAccessTokenRequest tokenRequest = (IAuthCodeAccessTokenRequest)oauthRequest;
		IPendingClientToken pendingClientToken = clientService.findPendingClientToken(tokenRequest.getClientId(),
				tokenRequest.getClientSecret(), tokenRequest.getCode());
		if (pendingClientToken == null) {
			LOGGER.error("no pending token found, client id {}", tokenRequest.getClientId());
			throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_GRANT, "client not authorized", null);
		}
		if (pendingClientToken.isExpired()) {
			LOGGER.debug("removing expired pending client token {}", tokenRequest.getClientId());
			clientService.removePendingClientToken(pendingClientToken);
			throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_GRANT, "client not authorized", null);
		}
		
		issueNewToken(request, response, pendingClientToken.getAuthorizedClient(), ResponseType.CODE, null);
		
		clientService.removePendingClientToken(pendingClientToken);
	}
	
	protected void refreshToken(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, IRefreshTokenRequest refreshTokenRequest)
			throws OAuth2ProtocolException, ResponseBuilderException, AuthorizationFlowException {
		String refreshToken = refreshTokenRequest.getRefreshToken();

		try {
			if (refreshTokenRequest.getScopes()!=null &&
				!refreshTokenRequest.getScopes().isEmpty()) {
				scopeValidator.validateScopes(refreshTokenRequest.getScopes());
			}
			
			IAccessTokenInfo oldTokenInfo = accessTokenService.getTokenInfoByRefreshToken(refreshToken);
			
			validateRefreshTokenRequest(oldTokenInfo, refreshTokenRequest);
			if (!checkScopeEnhancement(refreshTokenRequest, oldTokenInfo, request, response, servletContext))
			{
				String newAccessToken = tokenGenerator.createAccessToken();
				String newRefreshToken = issueRefreshToken?tokenGenerator.createRefreshToken():null;
	
				IAccessTokenInfo accessTokenInfo = accessTokenService.refreshToken(
						oldTokenInfo.getAccessToken(), newAccessToken, newRefreshToken);
				LOGGER.debug("token {} refreshed to {}", oldTokenInfo.getAccessToken(), newAccessToken);
	
				sendTokenResponse(request, response, accessTokenInfo, ResponseType.CODE, null);
			}
		} catch (InvalidTokenException e) {
			LOGGER.error("invalid token", e);
			throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_GRANT, "token is invalid", null, e);
		} catch (TokenStorageException e) {
			LOGGER.error("error with token storage", e);
			throw new OAuth2ProtocolException(OAuth2ErrorCode.SERVER_ERROR, "Server error", null, e);
		} catch (TokenGenerationException e) {
			LOGGER.error("error with token generation", e);
			throw new OAuth2ProtocolException(OAuth2ErrorCode.SERVER_ERROR, "Server error", null, e);
		} catch (InvalidScopeException e) {
			LOGGER.error("Scope is invalid", e);
			throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_SCOPE, "Scope is invalid", null, e);
		}
	}
	
	protected void validateRefreshTokenRequest(IAccessTokenInfo oldTokenInfo, IRefreshTokenRequest request) throws OAuth2ProtocolException
	{
		IRegisteredClientApp clientApp = clientService.getRegisteredClient(oldTokenInfo.getClientId());
		String clientSecret = clientApp.getClientSecret();
		if (clientSecret!=null)
		{
			if (!clientSecret.equals(request.getClientSecret()))
				throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_CLIENT, null);
		}
	}
	
	protected boolean checkScopeEnhancement(IRefreshTokenRequest refreshRequest, IAccessTokenInfo oldTokenInfo, HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext) throws OAuth2ProtocolException, AuthorizationFlowException
	{
		if (refreshRequest.getScopes()!=null)
		{
			if (!scopeValidator.isScopeEqual(refreshRequest.getScopes(), oldTokenInfo.getAuthorizedScopes()))
			{
				if (allowScopeEnhancement)
				{
					IRegisteredClientApp clientApp = clientService.getRegisteredClient(oldTokenInfo.getClientId());
					authFlow.startScopeEnhancementFlow(oldTokenInfo.getUser(), clientApp, refreshRequest.getScopes(), 
							refreshRequest, request, response, servletContext);
					return true;
				} else
				{
					LOGGER.error("Client wants to extend scope with refresh token, but this is disabled");
					throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_SCOPE, null);
				}
			}
		}
		return false;
	}

}
