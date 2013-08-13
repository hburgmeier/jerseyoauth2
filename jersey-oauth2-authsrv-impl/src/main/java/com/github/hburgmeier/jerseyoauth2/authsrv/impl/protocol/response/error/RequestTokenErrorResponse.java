package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.error;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ResponseFormat;

public class RequestTokenErrorResponse extends AbstractErrorResponse {

	public RequestTokenErrorResponse(int statusCode, OAuth2ProtocolException ex) {
		super(statusCode, ResponseFormat.JSON, ex);
	}

	@Override
	public void render(HttpServletResponse response) throws ResponseBuilderException {
		super.render(response);
		
		renderJson(errorEntity, response);
	}	
	
}
