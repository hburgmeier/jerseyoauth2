package com.burgmeier.jerseyoauth2.test.client;

import com.burgmeier.jerseyoauth2.client.scribe.BaseOAuth2Api;


public class LocalTestAPI extends BaseOAuth2Api {
	
	public LocalTestAPI(String grantType) {
		super(grantType);
	}

	@Override
	protected String getResponseType() {
		return "code";
	}

	@Override
	protected String getAccessTokenEndpointBase() {
		return "http://localhost:9998/testsuite/oauth2/accessToken";
	}

	@Override
	protected String getAuthorizationUrlBase() {
		return "http://localhost:9998/testsuite/oauth2/auth";
	}

}
