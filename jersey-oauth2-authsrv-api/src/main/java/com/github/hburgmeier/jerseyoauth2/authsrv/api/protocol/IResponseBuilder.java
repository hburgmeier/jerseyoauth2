package com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol;

import java.net.URI;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;

public interface IResponseBuilder {

	IOAuth2Response buildAuthorizationRequestErrorResponse(OAuth2ProtocolException ex, URI redirectUrl) throws ResponseBuilderException;
	
	IOAuth2Response buildRequestTokenErrorResponse(OAuth2ProtocolException ex) throws ResponseBuilderException;
	
	IOAuth2Response buildAuthorizationCodeResponse(String code, URI redirectUrl, String state) throws ResponseBuilderException;
	
	IOAuth2Response buildAccessTokenResponse(IAccessTokenInfo accessToken, String state) throws ResponseBuilderException;
	
	IOAuth2Response buildImplicitGrantAccessTokenResponse(IAccessTokenInfo accessToken, URI redirectUrl, String state) throws ResponseBuilderException;
	
	IOAuth2Response buildForwardResponse(String relativeUrl);

	IOAuth2Response buildUnauthorizedResponse(OAuth2ProtocolException ex);
}
