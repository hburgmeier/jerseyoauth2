package com.github.hburgmeier.jerseyoauth2.authsrv.impl.endpoints.rest;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IHttpContext;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpRequestAdapter;

public class TokenEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenEndpoint.class);	
	
	@Context 
	protected ServletContext servletContext;
	
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
	public Response issueToken(@Context HttpServletRequest request, @Context HttpServletResponse response)
	{
		if (configuration.getStrictSecurity() && !request.isSecure())
		{
			LOGGER.error("Strict security switch on but insecure request received");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			IHttpContext context = new RestHttpContext(request, response, servletContext);
			
			IAccessTokenRequest oauthRequest = null;
			try {
				oauthRequest = requestFactory.parseAccessTokenRequest(new HttpRequestAdapter(request), 
						configuration.getEnableAuthorizationHeaderForClientAuth());
				LOGGER.debug("Parsing OAuthTokenRequest successful");

				IOAuth2Response oauth2Response = tokenService.handleRequest(request, oauthRequest);
				oauth2Response.render(context);
			} catch (OAuth2ParseException e) {
				LOGGER.error("Token request problem", e);
				IOAuth2Response oauth2Response = tokenService.sendErrorResponse(oauthRequest, e);
				oauth2Response.render(context);
			} catch (OAuth2ProtocolException e) {
				LOGGER.error("Token request problem", e);
				IOAuth2Response oauth2Response = tokenService.sendErrorResponse(oauthRequest, e);
				oauth2Response.render(context);
				
			}
		} catch (AuthorizationFlowException | ResponseBuilderException e) {
			LOGGER.error("OAuth2 system exception", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return null;
	}
	
}
