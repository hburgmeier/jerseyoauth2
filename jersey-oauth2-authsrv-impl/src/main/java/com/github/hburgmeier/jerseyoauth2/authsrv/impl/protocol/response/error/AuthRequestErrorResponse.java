package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.error;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IHttpContext;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ResponseFormat;

public class AuthRequestErrorResponse extends AbstractErrorResponse {

	private final URI redirectUrl;

	public AuthRequestErrorResponse(int statusCode, URI redirectUrl, OAuth2ProtocolException ex) {
		super(statusCode, ResponseFormat.QUERY, ex);
		this.redirectUrl = redirectUrl;
	}

	@Override
	public void render(IHttpContext context) throws ResponseBuilderException {
		super.render(context);
		
		renderRedirect(errorEntity, redirectUrl, context);
	}
}
