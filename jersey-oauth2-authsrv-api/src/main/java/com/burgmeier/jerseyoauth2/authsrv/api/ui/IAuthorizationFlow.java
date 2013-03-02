package com.burgmeier.jerseyoauth2.authsrv.api.ui;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;

public interface IAuthorizationFlow {

	void startAuthorizationFlow(IUser user, IRegisteredClientApp clientApp, Set<String> scope, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
		throws AuthorizationFlowException, ServletException, IOException;

	void handleMissingUser(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext) throws AuthorizationFlowException, ServletException, IOException;
	
}
