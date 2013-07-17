package com.burgmeier.jerseyoauth2.testsuite.base.client;

import com.burgmeier.jerseyoauth2.client.scribe.BaseOAuth2Api;



public class TestsuiteAPI extends BaseOAuth2Api {
	
	private String responseType;

	public TestsuiteAPI(String grantType, String state, String responseType) {
		super(grantType, state);
		this.responseType = responseType;
	}

	@Override
	protected String getResponseType() {
		return responseType;
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
