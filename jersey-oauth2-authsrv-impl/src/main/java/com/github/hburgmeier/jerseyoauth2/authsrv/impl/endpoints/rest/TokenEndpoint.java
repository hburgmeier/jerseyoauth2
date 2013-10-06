package com.github.hburgmeier.jerseyoauth2.authsrv.impl.endpoints.rest;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ErrorCode;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpRequestAdapter;

public class TokenEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenEndpoint.class);	
	
	@Context 
	protected ServletContext context;
	
	private final ITokenService tokenService;
	private final IConfiguration configuration;
	private final IRequestFactory requestFactory;
	
	@Inject
	public TokenEndpoint(final ITokenService tokenService, final IConfiguration configuration, final IRequestFactory requestFactory)
	{
		this.tokenService = tokenService;
		this.configuration = configuration;
		this.requestFactory = requestFactory;
		
	}
	
	@POST
	public Response issueToken(@Context HttpServletRequest request)
	{
/*
		if (configuration.getStrictSecurity() && !request.isSecure())
		{
			LOGGER.error("Strict security switch on but insecure request received");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			IAccessTokenRequest oauthRequest = null;
			try {
				oauthRequest = requestFactory.parseAccessTokenRequest(new HttpRequestAdapter(request), 
						configuration.getEnableAuthorizationHeaderForClientAuth());
				LOGGER.debug("Parsing OAuthTokenRequest successful");

				tokenService.handleRequest(request, response, context, oauthRequest);
			} catch (OAuth2ParseException e) {
				LOGGER.error("Token request problem", e);
				tokenService.sendErrorResponse(response, e);
			} catch (OAuth2ProtocolException e) {
				LOGGER.error("Token request problem", e);
				if (e.getErrorCode() == OAuth2ErrorCode.INVALID_CLIENT &&
						oauthRequest.hasUsedAuhorizationHeader())
					{
						sendUnauthorizedResponse(response);
					} else {
						tokenService.sendErrorResponse(response, e);
					}
				
			}
		} catch (AuthorizationFlowException | ResponseBuilderException e) {
			LOGGER.error("OAuth2 system exception", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		*/
		return null;
	}
	
}
