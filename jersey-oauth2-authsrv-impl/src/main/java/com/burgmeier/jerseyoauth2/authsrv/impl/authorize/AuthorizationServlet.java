package com.burgmeier.jerseyoauth2.authsrv.impl.authorize;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IAuthorizationService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthorizationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final IAuthorizationService authService;
	private final IConfiguration configuration;
	
	@Inject
	public AuthorizationServlet(final IAuthorizationService authService, final IConfiguration configuration)
	{
		this.authService = authService;
		this.configuration = configuration;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (configuration.getStrictSecurity() && !request.isSecure())
		{
			response.sendError(400);
		} else
			authService.evaluateAuthorizationRequest(request, response, getServletContext());
	}

}
