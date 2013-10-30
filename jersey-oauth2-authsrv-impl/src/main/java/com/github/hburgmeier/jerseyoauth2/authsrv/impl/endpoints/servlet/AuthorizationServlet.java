package com.github.hburgmeier.jerseyoauth2.authsrv.impl.endpoints.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.authorization.IAuthorizationService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IHttpContext;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.HttpServletContextImplementation;
import com.google.inject.Singleton;

@Singleton
public class AuthorizationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServlet.class);
	
	private final IAuthorizationService authService;
	
	@Inject
	public AuthorizationServlet(final IAuthorizationService authService)
	{
		this.authService = authService;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			IOAuth2Response oauth2Response = authService.evaluateAuthorizationRequest(request, response, getServletContext());
			IHttpContext context = new HttpServletContextImplementation(request, response, getServletContext());
			oauth2Response.render(context);
		} catch (AuthorizationFlowException e) {
			LOGGER.error("Error in authorization flow",e);
			throw new ServletException(e.getMessage(), e);
		} catch (ResponseBuilderException e) {
			LOGGER.error("Error in OAuth2 Protocol",e);
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			IOAuth2Response oauth2Response = authService.evaluateAuthorizationRequest(request, response, getServletContext());
			IHttpContext context = new HttpServletContextImplementation(request, response, getServletContext());
			oauth2Response.render(context);
		} catch (AuthorizationFlowException e) {
			LOGGER.error("Error in authorization flow",e);
			throw new ServletException(e.getMessage(), e);
		} catch (ResponseBuilderException e) {
			LOGGER.error("Error in OAuth2 Protocol",e);
			throw new ServletException(e);
		}
	}

}
