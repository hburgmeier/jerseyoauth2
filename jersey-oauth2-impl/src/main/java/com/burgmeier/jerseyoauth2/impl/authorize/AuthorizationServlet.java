package com.burgmeier.jerseyoauth2.impl.authorize;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;

import com.burgmeier.jerseyoauth2.api.client.IClientAuthorization;
import com.burgmeier.jerseyoauth2.api.client.IClientService;
import com.burgmeier.jerseyoauth2.api.client.IRegisteredClientApp;
import com.burgmeier.jerseyoauth2.api.ui.AuthorizationFlowException;
import com.burgmeier.jerseyoauth2.api.ui.IAuthorizationFlow;
import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.api.user.IUserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthorizationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final IClientService clientService;
	private final IUserService userService;
	private final IAuthorizationFlow authFlow;
	
	private ServletContext servletContext;
	
	
	@Inject
	public AuthorizationServlet(final IClientService clientService, final IUserService userService,
			final IAuthorizationFlow authFlow, ServletContext servletContext)
	{
		this.clientService = clientService;
		this.userService = userService;
		this.authFlow = authFlow;
		this.servletContext = servletContext;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			IRegisteredClientApp clientApp = null;
			try {
				OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
				
				IUser user = userService.getCurrentUser(request);
				if (user==null)
					throw new InvalidUserException();
				
				clientApp = clientService.getRegisteredClient(oauthRequest.getClientId());
				if (clientApp==null)
					throw new ClientNotFoundException(oauthRequest.getClientId());
				
				IClientAuthorization clientAuth = clientService.isAuthorized(user, clientApp.getClientId(), oauthRequest.getScopes());
				if (clientAuth!=null)
				{
					OAuthResponse resp = OAuthASResponse
				        .authorizationResponse(request, HttpServletResponse.SC_FOUND)
				        .setCode(clientAuth.getCode())                    
				        .location(clientAuth.getRedirectUrl())
				        .buildQueryMessage();

					response.sendRedirect(resp.getLocationUri());
				} else {
					authFlow.startAuthorizationFlow(user, clientApp, oauthRequest.getScopes(), request, response, servletContext);
				}
			} catch (OAuthSystemException e) {
				throw new ServletException();
			} catch (OAuthProblemException e) {
				try {
					sendErrorResponse(e, response, "testurl");
				} catch (OAuthSystemException e1) {
					throw new ServletException(e1);
				}
			} catch (ClientNotFoundException e) {
				authFlow.handleMissingClientRegistration(e.getClientId(), request, response, servletContext);
			} catch (InvalidUserException e) {
				authFlow.handleMissingUser(request, response, servletContext);
			}
		} catch (AuthorizationFlowException e) {
			throw new ServletException(e);
		}		
	}

	private void sendErrorResponse(OAuthProblemException ex,
			HttpServletResponse response, String redirectUri) throws OAuthSystemException, IOException {
        final OAuthResponse resp = OAuthASResponse
                .errorResponse(HttpServletResponse.SC_FOUND)
                .error(ex)
                .location(redirectUri)
                .buildQueryMessage();
                   
        response.sendRedirect(resp.getLocationUri());
	}

}
