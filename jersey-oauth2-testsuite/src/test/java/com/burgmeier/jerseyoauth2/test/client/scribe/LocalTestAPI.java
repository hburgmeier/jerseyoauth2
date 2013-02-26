package com.burgmeier.jerseyoauth2.test.client.scribe;

import java.text.MessageFormat;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;

public class LocalTestAPI extends DefaultApi20 {

	private AccessTokenExtractor tokenExtractor = new OAuth20TokenExtractorImpl();

	@Override
	public String getAccessTokenEndpoint() {
		return "http://localhost:9998/example1/oauth2/accessToken?grant_type=authorization_code";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		return MessageFormat.format("http://localhost:9998/example1/oauth2/auth?response_type=code&client_id={0}",
				config.getApiKey());
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return tokenExtractor ;
	}

}
