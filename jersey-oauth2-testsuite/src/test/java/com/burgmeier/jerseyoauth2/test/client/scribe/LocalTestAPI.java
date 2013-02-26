package com.burgmeier.jerseyoauth2.test.client.scribe;


public class LocalTestAPI extends BaseOAuth2Api {

	@Override
	protected String getGrantType() {
		return "authorization_code";
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
