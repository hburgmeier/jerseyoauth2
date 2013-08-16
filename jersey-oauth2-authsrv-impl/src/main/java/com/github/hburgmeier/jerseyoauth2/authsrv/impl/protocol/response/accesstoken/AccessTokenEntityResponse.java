package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ResponseFormat;

public class AccessTokenEntityResponse extends AbstractAccessTokenResponse {

	public AccessTokenEntityResponse(int statusCode, IAccessTokenInfo accessToken, String state) {
		super(statusCode, ResponseFormat.JSON, accessToken, state);
	}

	@Override
	public void render(HttpServletResponse response) throws ResponseBuilderException {
		super.render(response);
		
		renderJson(tokenInfo, response);
	}
}
