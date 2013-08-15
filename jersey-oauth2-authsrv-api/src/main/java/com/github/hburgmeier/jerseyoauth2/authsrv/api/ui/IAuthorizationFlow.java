package com.github.hburgmeier.jerseyoauth2.authsrv.api.ui;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRefreshTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;

public interface IAuthorizationFlow {

	void startAuthorizationFlow(IUser user, IRegisteredClientApp clientApp, Set<String> scope, IAuthorizationRequest originalRequest, 
			HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
		throws AuthorizationFlowException, ServletException, IOException;
	
	void startScopeEnhancementFlow(IUser user, IRegisteredClientApp clientApp, Set<String> requestedScope, IRefreshTokenRequest refreshTokenRequest, 
			HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
		throws AuthorizationFlowException;

	void handleMissingUser(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) 
		throws AuthorizationFlowException, ServletException, IOException;

	void handleInvalidRedirectUrl(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) 
		throws AuthorizationFlowException, ServletException, IOException;
	
}
