package com.burgmeier.jerseyoauth2.authsrv.impl.services;

import java.io.IOException;
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
import org.apache.amber.oauth2.common.message.types.ResponseType;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientType;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IAuthorizationService;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IPendingClientToken;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.burgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.burgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.burgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;
import com.burgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.burgmeier.jerseyoauth2.authsrv.impl.amber.OAuth2AuthzRequest;
import com.burgmeier.jerseyoauth2.authsrv.impl.authorize.InvalidUserException;
import com.google.inject.Inject;

public class AuthorizationService implements IAuthorizationService {

	private final IClientService clientService;
	private final IUserService userService;
	private final IAuthorizationFlow authFlow;
	private final IConfiguration configuration;
	private final ITokenService tokenService;
	
	@Inject
	public AuthorizationService(final IClientService clientService, final IUserService userService,
			final IAuthorizationFlow authFlow, final IConfiguration configuration, final ITokenService tokenService)
	{
		this.clientService = clientService;
		this.userService = userService;
		this.authFlow = authFlow;
		this.configuration = configuration;
		this.tokenService = tokenService;
	}	
	
	@Override
	public void evaluateAuthorizationRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ServletException, IOException {
		try {
			IRegisteredClientApp regClientApp = null;
			try {
				OAuth2AuthzRequest oauthRequest = new OAuth2AuthzRequest(request, configuration.getSupportAuthorizationHeader());
				
				IUser user = userService.getCurrentUser(request);
				if (user==null)
					throw new InvalidUserException();
				
				regClientApp = clientService.getRegisteredClient(oauthRequest.getClientId());
				if (regClientApp==null)
					throw OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client "+oauthRequest.getClientId()+" is invalid");

				Set<String> scopes = oauthRequest.getScopes();
				scopes = scopes.isEmpty()?configuration.getDefaultScopes():scopes;
				
				ResponseType reqResponseType = oauthRequest.getResponseType().equals(ResponseType.TOKEN.toString())?
						ResponseType.TOKEN:ResponseType.CODE;
				
				if (reqResponseType.equals(ResponseType.CODE))
				{
					if (oauthRequest.getClientSecret()!=null)
					{
						if (!regClientApp.getClientSecret().equals(oauthRequest.getClientSecret()))
							throw OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client is invalid");
					}
				}
				
				if (regClientApp.getClientType().equals(ClientType.CONFIDENTIAL) && 
					reqResponseType.equals(ResponseType.TOKEN))
					throw OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client type is invalid");
System.err.println("request "+oauthRequest.getClientId()+" "+scopes);					
				IAuthorizedClientApp authorizedClientApp = clientService.isAuthorized(user, regClientApp.getClientId(), scopes);
				if (authorizedClientApp!=null)
				{
					try {
						if (reqResponseType.equals(ResponseType.CODE))
						{
							IPendingClientToken pendingClientToken = clientService.createPendingClientToken(authorizedClientApp);
							sendAuthorizationReponse(request, response, pendingClientToken, regClientApp);
						} else {
							tokenService.issueNewToken(request, response, authorizedClientApp, reqResponseType);
						}
					} catch (ClientServiceException e) {
						throw OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client is invalid");
					}
				} else {
					authFlow.startAuthorizationFlow(user, regClientApp, scopes, request, response, servletContext);
				}
			} catch (OAuthSystemException e) {
				throw new ServletException();
			} catch (OAuthProblemException e) {
e.printStackTrace();				
				try {
					sendErrorResponse(e, response, regClientApp==null?null:regClientApp.getCallbackUrl());
				} catch (OAuthSystemException e1) {
					throw new ServletException(e1);
				}
			} catch (InvalidUserException e) {
				authFlow.handleMissingUser(request, response, servletContext);
			}
		} catch (AuthorizationFlowException e) {
			throw new ServletException(e);
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

}
