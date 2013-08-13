package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;


public interface IOAuth2Response {

	void render(HttpServletResponse response) throws ResponseBuilderException;
	
}
