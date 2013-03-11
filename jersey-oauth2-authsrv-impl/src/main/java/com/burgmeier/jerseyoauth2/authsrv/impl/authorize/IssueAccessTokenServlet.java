package com.burgmeier.jerseyoauth2.authsrv.impl.authorize;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

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
		} else {
			
			try {
				try {
					OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

					tokenService.handleRequest(request, response, oauthRequest);

					// if something goes wrong
				} catch (OAuthProblemException ex) {
					tokenService.sendErrorResponse(response, ex);
				}
			} catch (OAuthSystemException e) {
				throw new ServletException(e);
			}
		}
	}

}
