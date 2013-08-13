package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.api;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;

public interface IResponseBuilder {

	void buildRequestTokenErrorResponse(OAuth2ProtocolException ex, HttpServletResponse response) throws ResponseBuilderException;
	
	void buildAccessTokenResponse(IAccessTokenInfo accessToken, String state, HttpServletResponse response) throws ResponseBuilderException;
	
	void buildImplicitGrantAccessTokenResponse(IAccessTokenInfo accessToken, URI redirectUrl, String state, 
			HttpServletResponse response) throws ResponseBuilderException;
}
