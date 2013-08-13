package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ResponseFormat;

public class AccessTokenPostResponse extends AbstractAccessTokenResponse {

	public AccessTokenPostResponse(int statusCode, ResponseFormat responseFormat, IAccessTokenInfo accessToken, String state) {
		super(statusCode, responseFormat, accessToken, state);
	}

	@Override
	public void render(HttpServletResponse response) throws ResponseBuilderException {
		super.render(response);
		
		renderJson(tokenInfo, response);
	}
}
