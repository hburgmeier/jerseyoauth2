package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.api.IResponseBuilder;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken.AccessTokenPostResponse;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken.AccessTokenRedirectResponse;

public class ResponseBuilder implements IResponseBuilder {

	@Override
	public void buildRequestTokenErrorResponse(OAuth2ProtocolException ex, HttpServletResponse response) throws ResponseBuilderException {
		OAuth2ErrorResponse errorResponse = new OAuth2ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, ResponseFormat.JSON, ex);
		errorResponse.render(response);
	}
	
	@Override
	public void buildAccessTokenResponse(IAccessTokenInfo accessToken, String state, HttpServletResponse response)
			throws ResponseBuilderException {
		AccessTokenPostResponse oauthResponse = new AccessTokenPostResponse(HttpServletResponse.SC_OK, ResponseFormat.JSON, accessToken, state);
		oauthResponse.render(response);
	}
	
	@Override
	public void buildImplicitGrantAccessTokenResponse(IAccessTokenInfo accessToken, URI redirectUrl, String state, HttpServletResponse response)
			throws ResponseBuilderException {
		AccessTokenRedirectResponse oauthResponse = new AccessTokenRedirectResponse(HttpServletResponse.SC_MOVED_TEMPORARILY, ResponseFormat.QUERY, 
				accessToken, redirectUrl, state);
		oauthResponse.render(response);
	}

}
