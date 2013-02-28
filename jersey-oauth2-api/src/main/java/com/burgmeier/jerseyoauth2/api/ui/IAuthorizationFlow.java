package com.burgmeier.jerseyoauth2.api.ui;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.burgmeier.jerseyoauth2.api.client.IRegisteredClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;

public interface IAuthorizationFlow {

	void startAuthorizationFlow(IUser user, IRegisteredClientApp clientApp, Set<String> scope, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
		throws AuthorizationFlowException, ServletException, IOException;

	void handleMissingUser(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext) throws AuthorizationFlowException, ServletException, IOException;
	
}
