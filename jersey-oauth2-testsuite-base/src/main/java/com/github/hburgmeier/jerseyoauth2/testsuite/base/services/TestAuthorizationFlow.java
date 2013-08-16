package com.github.hburgmeier.jerseyoauth2.testsuite.base.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRefreshTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizationService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;

public class TestAuthorizationFlow implements IAuthorizationFlow {

	private final IClientService clientService;
	private final IAuthorizationService authorizationService;

	@Inject
	public TestAuthorizationFlow(IClientService clientService, IAuthorizationService authorizationService)
	{
		this.clientService = clientService;
		this.authorizationService = authorizationService;
	}
	
	@Override
	public void startAuthorizationFlow(IUser user, IRegisteredClientApp clientApp, Set<String> scope, IAuthorizationRequest originalRequest, 
			HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws AuthorizationFlowException, ServletException, IOException {
		
		try {
			try {
				IAuthorizedClientApp authorizedClient = clientService.authorizeClient(user, clientApp, scope);
				authorizationService.sendAuthorizationReponse(request, response, originalRequest, clientApp, authorizedClient);
			} catch (OAuth2ProtocolException e) {
				authorizationService.sendErrorResponse(e, response, new URI(clientApp.getCallbackUrl()));
			}
		} catch (URISyntaxException | ClientServiceException | ResponseBuilderException e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void handleMissingUser(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext)
			throws AuthorizationFlowException, ServletException, IOException {
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/error.jsp");
		requestDispatcher.forward(request, response);
	}

	@Override
	public void handleInvalidRedirectUrl(HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext) throws AuthorizationFlowException, ServletException, IOException {
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/error.jsp");
		requestDispatcher.forward(request, response);
	}
	
	@Override
	public void startScopeEnhancementFlow(IUser user, IRegisteredClientApp clientApp, Set<String> requestedScope,
			IRefreshTokenRequest refreshTokenRequest, HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext) throws AuthorizationFlowException {
		try {
			RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/error.jsp");
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new AuthorizationFlowException(e);
		}
	}	

}
