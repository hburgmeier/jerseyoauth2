package com.github.hburgmeier.jerseyoauth2.sample.services;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRefreshTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ForwardOAuth2Response;

public class TestAuthorizationFlow implements IAuthorizationFlow {

	@Override
	public IOAuth2Response startAuthorizationFlow(IUser user, IRegisteredClientApp clientApp, Set<String> scope, IAuthorizationRequest originalRequest, 
			HttpServletRequest request)
			throws AuthorizationFlowException, ServletException, IOException {
		ForwardOAuth2Response response = new ForwardOAuth2Response("/auth.jsp");
		response.addRequestAttribute("clientApp", clientApp);
		response.addRequestAttribute("scope", scope);
		
		StringBuffer scopesBuf = new StringBuffer();
		for (String scopeItem : scope)
		{
			scopesBuf.append(scopeItem).append(" ");
		}
		response.addRequestAttribute("scopes", scopesBuf.toString());
		
		return response;
	}

	@Override
	public IOAuth2Response handleMissingUser(HttpServletRequest request)
			throws AuthorizationFlowException, ServletException, IOException {
		return new ForwardOAuth2Response("/error.jsp");
	}

	@Override
	public IOAuth2Response handleInvalidClient(HttpServletRequest request) throws AuthorizationFlowException, ServletException, IOException {
		return new ForwardOAuth2Response("/error.jsp");
	}
	
	@Override
	public IOAuth2Response startScopeEnhancementFlow(IUser user, IRegisteredClientApp clientApp, Set<String> requestedScope,
			IRefreshTokenRequest refreshTokenRequest, HttpServletRequest request) throws AuthorizationFlowException {
		return new ForwardOAuth2Response("/error.jsp");
	}	

}
