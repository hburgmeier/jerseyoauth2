package com.github.hburgmeier.jerseyoauth2.authsrv.api.token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.api.types.ResponseType;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;

public interface ITokenService {
	
	void handleRequest(HttpServletRequest request, HttpServletResponse response, IAccessTokenRequest oauthRequest)
			throws OAuth2ProtocolException, ResponseBuilderException;
	
	void issueNewToken(HttpServletRequest request, HttpServletResponse response, IAuthorizedClientApp clientApp, ResponseType responseType, String state)
			throws OAuth2ProtocolException, ResponseBuilderException;
	
	void sendTokenResponse(HttpServletRequest request, HttpServletResponse response, IAccessTokenInfo accessTokenInfo, ResponseType responseType, String state) 
			 throws ResponseBuilderException;
	
	void sendErrorResponse(HttpServletResponse response, OAuth2ProtocolException ex) throws ResponseBuilderException;

	
}
