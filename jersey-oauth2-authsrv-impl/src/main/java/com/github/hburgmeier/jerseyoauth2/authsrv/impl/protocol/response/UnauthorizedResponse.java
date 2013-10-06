package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpHeaders;

public class UnauthorizedResponse extends AbstractOAuth2Response implements
		IOAuth2Response {

	public UnauthorizedResponse() {
		super(HttpServletResponse.SC_UNAUTHORIZED, ResponseFormat.QUERY);
		setHeader(HttpHeaders.AUTHENTICATE, "Basic");
	}
	
	
	
}
