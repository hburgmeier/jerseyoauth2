package com.github.hburgmeier.jerseyoauth2.sample.services;

import java.io.IOException;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;

public class TestAuthorizationFlow implements IAuthorizationFlow {

	@Override
	public void startAuthorizationFlow(IUser user,
			IRegisteredClientApp clientApp, Set<String> scope,
			HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws AuthorizationFlowException, ServletException, IOException {
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/auth.jsp");
		request.setAttribute("clientApp", clientApp);
		request.setAttribute("scope", scope);
		
		StringBuffer scopesBuf = new StringBuffer();
		for (String scopeItem : scope)
		{
			scopesBuf.append(scopeItem).append(" ");
		}
		request.setAttribute("scopes", scopesBuf.toString());
		
		requestDispatcher.forward(request, response);
	}

	@Override
	public void handleMissingUser(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext)
			throws AuthorizationFlowException, ServletException, IOException {
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/error.jsp");
		requestDispatcher.forward(request, response);
	}



}
