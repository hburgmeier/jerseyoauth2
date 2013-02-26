package com.burgmeier.jerseyoauth2.client.scribe;

import java.text.MessageFormat;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;

public abstract class BaseOAuth2Api extends DefaultApi20 {
	
	private final AccessTokenExtractor tokenExtractor = new OAuth20TokenExtractorImpl();
	
	@Override
	public final AccessTokenExtractor getAccessTokenExtractor() {
		return tokenExtractor;
	}
	
	@Override
	public final Verb getAccessTokenVerb() {
		return Verb.POST;
	}
	
	@Override
	public final String getAccessTokenEndpoint() {
		return MessageFormat.format("{1}?grant_type={0}",
				getGrantType(),
				getAccessTokenEndpointBase());
	}

	@Override
	public final String getAuthorizationUrl(OAuthConfig config) {
		return MessageFormat.format("{2}?response_type={1}&client_id={0}",
				config.getApiKey(), 
				getResponseType(),
				getAuthorizationUrlBase());
	}	
	
	protected abstract String getGrantType();
	
	protected abstract String getResponseType();
	
	protected abstract String getAccessTokenEndpointBase();
	
	protected abstract String getAuthorizationUrlBase();

	
}
