package com.github.hburgmeier.jerseyoauth2.authsrv.api.ui;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRefreshTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;

public interface IAuthorizationFlow {

	IOAuth2Response startAuthorizationFlow(IUser user, IRegisteredClientApp clientApp, Set<String> scope, IAuthorizationRequest originalRequest, 
			HttpServletRequest request)
		throws AuthorizationFlowException, ServletException, IOException;
	
	IOAuth2Response startScopeEnhancementFlow(IUser user, IRegisteredClientApp clientApp, Set<String> requestedScope, IRefreshTokenRequest refreshTokenRequest, 
			HttpServletRequest request)
		throws AuthorizationFlowException;

	IOAuth2Response handleMissingUser(HttpServletRequest request) 
		throws AuthorizationFlowException, ServletException, IOException;

	IOAuth2Response handleInvalidClient(HttpServletRequest request) 
		throws AuthorizationFlowException, ServletException, IOException;
	
}
