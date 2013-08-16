package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ResponseFormat;

public class AccessTokenRedirectResponse extends AbstractAccessTokenResponse {

	private URI redirectUrl;

	public AccessTokenRedirectResponse(int statusCode, IAccessTokenInfo accessToken, URI redirectUrl, String state) {
		super(statusCode, ResponseFormat.QUERY, accessToken, state);
		this.redirectUrl = redirectUrl;
	}

	@Override
	public void render(HttpServletResponse response) throws ResponseBuilderException {
		super.render(response);
		
		renderRedirectWithFragment(tokenInfo, redirectUrl, response);
	}
	
}
