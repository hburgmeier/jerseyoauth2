package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IHttpContext;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ResponseFormat;

public class AccessTokenEntityResponse extends AbstractAccessTokenResponse {

	public AccessTokenEntityResponse(int statusCode, IAccessTokenInfo accessToken, String state) {
		super(statusCode, ResponseFormat.JSON, accessToken, state);
	}

	@Override
	public void render(IHttpContext context) throws ResponseBuilderException {
		super.render(context);
		
		renderJson(tokenInfo, context);
	}
}
