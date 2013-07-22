package com.burgmeier.jerseyoauth2.authsrv.impl.authorize;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IAuthorizationService;
import com.burgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthorizationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServlet.class);
	
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
			LOGGER.error("Strict security switch on but insecure request received");
			response.sendError(400);
		} else {
			try {
				authService.evaluateAuthorizationRequest(request, response, getServletContext());
			} catch (AuthorizationFlowException e) {
				LOGGER.error("Error in authorization flow",e);
				throw new ServletException(e.getMessage(), e);
			} catch (OAuthSystemException e) {
				LOGGER.error("Error in OAuth2 Protocol",e);
				throw new ServletException(e);
			}
		}
	}

}
