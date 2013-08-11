package com.github.hburgmeier.jerseyoauth2.authsrv.impl.authorize;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2Exception;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpRequestAdapter;
import com.google.inject.Singleton;

@Singleton
public class IssueAccessTokenServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IssueAccessTokenServlet.class);
	
	private final ITokenService tokenService;
	private final IConfiguration configuration;
	private final IRequestFactory requestFactory;
	
	@Inject
	public IssueAccessTokenServlet(final ITokenService tokenService, final IConfiguration configuration, final IRequestFactory requestFactory) {
		this.tokenService = tokenService;
		this.configuration = configuration;
		this.requestFactory = requestFactory;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		if (configuration.getStrictSecurity() && !request.isSecure())
		{
			LOGGER.error("Strict security switch on but insecure request received");
			response.sendError(HttpURLConnection.HTTP_BAD_REQUEST);
		} else {
			
			try {
				try {
					IAccessTokenRequest oauthRequest = requestFactory.parseAccessTokenRequest(new HttpRequestAdapter(request), 
							configuration.getEnableAuthorizationHeaderForClientAuth());
//					OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
					LOGGER.debug("Parsing OAuthTokenRequest successful");

					tokenService.handleRequest(request, response, oauthRequest);

					// if something goes wrong
				} catch (OAuthProblemException ex) {
					LOGGER.error("Token request problem", ex);
					tokenService.sendErrorResponse(response, ex);
				} catch (OAuth2Exception e) {
					LOGGER.error("Token request problem", e);
					//TODO
//					tokenService.sendErrorResponse(response, ex);
				}
			} catch (OAuthSystemException e) {
				LOGGER.error("OAuth2 system exception", e);
				throw new ServletException(e);
			}
		}
	}

}
