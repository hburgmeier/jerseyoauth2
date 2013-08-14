package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.api.IResponseBuilder;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken.AccessTokenPostResponse;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken.AccessTokenRedirectResponse;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.authcode.AuthCodeResponse;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.error.AuthRequestErrorResponse;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.error.RequestTokenErrorResponse;

public class ResponseBuilder implements IResponseBuilder {

	@Override
	public void buildRequestTokenErrorResponse(OAuth2ProtocolException ex, HttpServletResponse response)
			throws ResponseBuilderException {
		RequestTokenErrorResponse errorResponse = new RequestTokenErrorResponse(HttpServletResponse.SC_BAD_REQUEST, ex);
		errorResponse.render(response);
	}

	@Override
	public void buildAuthorizationRequestErrorResponse(OAuth2ProtocolException ex, URI redirectUrl,
			HttpServletResponse response) throws ResponseBuilderException {
		AuthRequestErrorResponse errorResponse = new AuthRequestErrorResponse(HttpServletResponse.SC_MOVED_TEMPORARILY,
				redirectUrl, ex);
		errorResponse.render(response);
	}

	@Override
	public void buildAuthorizationCodeResponse(String code, URI redirectUrl, String state, HttpServletResponse response)
			throws ResponseBuilderException {
		AuthCodeResponse oauthResponse = new AuthCodeResponse(HttpServletResponse.SC_MOVED_TEMPORARILY, code,
				redirectUrl, state);
		oauthResponse.render(response);
	}

	@Override
	public void buildAccessTokenResponse(IAccessTokenInfo accessToken, String state, HttpServletResponse response)
			throws ResponseBuilderException {
		AccessTokenPostResponse oauthResponse = new AccessTokenPostResponse(HttpServletResponse.SC_OK,
				ResponseFormat.JSON, accessToken, state);
		oauthResponse.render(response);
	}

	@Override
	public void buildImplicitGrantAccessTokenResponse(IAccessTokenInfo accessToken, URI redirectUrl, String state,
			HttpServletResponse response) throws ResponseBuilderException {
		AccessTokenRedirectResponse oauthResponse = new AccessTokenRedirectResponse(
				HttpServletResponse.SC_MOVED_TEMPORARILY, ResponseFormat.QUERY, accessToken, redirectUrl, state);
		oauthResponse.render(response);
	}

}
