package com.github.hburgmeier.jerseyoauth2.testsuite.base.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRefreshTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.authorization.IAuthorizationService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IResponseBuilder;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;

public class TestAuthorizationFlow implements IAuthorizationFlow {

	private final IClientService clientService;
	private final IAuthorizationService authorizationService;
	private final IResponseBuilder responseBuilder;

	@Inject
	public TestAuthorizationFlow(IClientService clientService, IAuthorizationService authorizationService, IResponseBuilder responseBuilder)
	{
		this.clientService = clientService;
		this.authorizationService = authorizationService;
		this.responseBuilder = responseBuilder;
	}
	
	@Override
	public IOAuth2Response startAuthorizationFlow(IUser user, IRegisteredClientApp clientApp, Set<String> scope, IAuthorizationRequest originalRequest, 
			HttpServletRequest request)
			throws AuthorizationFlowException, ServletException, IOException {
		
		try {
			try {
				IAuthorizedClientApp authorizedClient = clientService.authorizeClient(user, clientApp, scope);
				return authorizationService.sendAuthorizationResponse(request, originalRequest, clientApp, authorizedClient);
			} catch (OAuth2ProtocolException e) {
				return authorizationService.sendErrorResponse(e, new URI(clientApp.getCallbackUrl()));
			}
		} catch (URISyntaxException | ClientServiceException | ResponseBuilderException e) {
			throw new ServletException(e);
		}
	}

	@Override
	public IOAuth2Response handleMissingUser(HttpServletRequest request)
			throws AuthorizationFlowException, ServletException, IOException {
		return responseBuilder.buildForwardResponse("/error.jsp");
	}

	@Override
	public IOAuth2Response handleInvalidClient(HttpServletRequest request) throws AuthorizationFlowException, ServletException, IOException {
		return responseBuilder.buildForwardResponse("/error.jsp");
	}
	
	@Override
	public IOAuth2Response startScopeEnhancementFlow(IUser user, IRegisteredClientApp clientApp, Set<String> requestedScope,
			IRefreshTokenRequest refreshTokenRequest, HttpServletRequest request) throws AuthorizationFlowException {
		return responseBuilder.buildForwardResponse("/error.jsp");
	}	

}
