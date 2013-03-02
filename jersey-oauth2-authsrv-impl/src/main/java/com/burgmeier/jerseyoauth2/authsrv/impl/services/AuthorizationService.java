package com.burgmeier.jerseyoauth2.authsrv.impl.services;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.error.OAuthError.TokenResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;

import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IAuthorizationService;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientAuthorization;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.burgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.burgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;
import com.burgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.burgmeier.jerseyoauth2.authsrv.impl.authorize.InvalidUserException;
import com.google.inject.Inject;

public class AuthorizationService implements IAuthorizationService {

	private final IClientService clientService;
	private final IUserService userService;
	private final IAuthorizationFlow authFlow;
	private final IConfiguration configuration;
	
	@Inject
	public AuthorizationService(final IClientService clientService, final IUserService userService,
			final IAuthorizationFlow authFlow, final IConfiguration configuration)
	{
		this.clientService = clientService;
		this.userService = userService;
		this.authFlow = authFlow;
		this.configuration = configuration;
	}	
	
	@Override
	public void evaluateAuthorizationRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ServletException, IOException {
		try {
			IRegisteredClientApp clientApp = null;
			try {
				OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
				
				IUser user = userService.getCurrentUser(request);
				if (user==null)
					throw new InvalidUserException();
				
				clientApp = clientService.getRegisteredClient(oauthRequest.getClientId());
				if (clientApp==null)
					OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client is invalid");
				
				if (oauthRequest.getClientSecret()!=null)
				{
					if (!clientApp.getClientSecret().equals(oauthRequest.getClientSecret()))
						OAuthProblemException.error(TokenResponse.INVALID_CLIENT, "client is invalid");
				}
				
				Set<String> scopes = oauthRequest.getScopes();
				scopes = scopes.isEmpty()?configuration.getDefaultScopes():scopes;
				
				IClientAuthorization clientAuth = clientService.isAuthorized(user, clientApp.getClientId(), scopes);
				if (clientAuth!=null)
				{
					sendAuthorizationReponse(request, response, clientAuth, clientApp);
				} else {
					authFlow.startAuthorizationFlow(user, clientApp, scopes, request, response, servletContext);
				}
			} catch (OAuthSystemException e) {
				throw new ServletException();
			} catch (OAuthProblemException e) {
				try {
					sendErrorResponse(e, response, clientApp==null?null:clientApp.getCallbackUrl());
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
			IClientAuthorization clientAuth, IRegisteredClientApp clientApp) throws OAuthSystemException, IOException {
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
