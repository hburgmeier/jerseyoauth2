package com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;


public interface IOAuth2Response {
	
	void render(IHttpContext context) throws ResponseBuilderException;
	
}
