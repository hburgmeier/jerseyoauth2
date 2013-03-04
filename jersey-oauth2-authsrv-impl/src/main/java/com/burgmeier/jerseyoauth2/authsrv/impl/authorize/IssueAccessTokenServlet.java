package com.burgmeier.jerseyoauth2.authsrv.impl.authorize;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IssueAccessTokenServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ITokenService tokenService;
	private final IConfiguration configuration;
	
	@Inject
	public IssueAccessTokenServlet(final ITokenService tokenService, final IConfiguration configuration) {
		this.tokenService = tokenService;
		this.configuration = configuration;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		if (configuration.getStrictSecurity() && !request.isSecure())
		{
			response.sendError(400);
		} else
			tokenService.issueToken(request, response);
	}

}
