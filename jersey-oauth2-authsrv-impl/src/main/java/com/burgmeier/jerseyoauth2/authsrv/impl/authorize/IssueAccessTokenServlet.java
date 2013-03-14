package com.burgmeier.jerseyoauth2.authsrv.impl.authorize;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger logger = LoggerFactory.getLogger(IssueAccessTokenServlet.class);
	
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
			logger.error("Strict security switch on but insecure request received");
			response.sendError(400);
		} else {
			
			try {
				try {
					OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
					logger.debug("Parsing OAuthTokenRequest successful");

					tokenService.handleRequest(request, response, oauthRequest);

					// if something goes wrong
				} catch (OAuthProblemException ex) {
					logger.error("Token request problem", ex);
					tokenService.sendErrorResponse(response, ex);
				}
			} catch (OAuthSystemException e) {
				logger.error("OAuth2 system exception", e);
				throw new ServletException(e);
			}
		}
	}

}
