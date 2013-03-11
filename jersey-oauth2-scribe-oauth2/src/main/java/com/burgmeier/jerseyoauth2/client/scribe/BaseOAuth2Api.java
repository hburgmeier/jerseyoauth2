package com.burgmeier.jerseyoauth2.client.scribe;

import java.text.MessageFormat;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.ParameterList;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public abstract class BaseOAuth2Api extends DefaultApi20 {
	
	private final AccessTokenExtractor tokenExtractor = new OAuth20TokenExtractorImpl();
	private String grantType;
	private String state;
	
	public BaseOAuth2Api(String grantType, String state)
	{
		this.grantType = grantType;
		this.state = state;
		
	}
	
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
				grantType,
				getAccessTokenEndpointBase());
	}

	@Override
	public final String getAuthorizationUrl(OAuthConfig config) {
		ParameterList paramList = new ParameterList();
		if (state!=null)
			paramList.add("state", state);
		paramList.add("response_type", getResponseType());
		paramList.add("client_id", config.getApiKey());
		if (config.getCallback()!=null)
			paramList.add("redirect_uri", config.getCallback()); // for implicit grant
		return paramList.appendTo(getAuthorizationUrlBase());
	}	
	
	@Override
	public OAuthService createService(OAuthConfig config) {
		return new OAuth2ServiceWrapper(super.createService(config), this, config);
	}

	protected abstract String getResponseType();
	
	protected abstract String getAccessTokenEndpointBase();
	
	protected abstract String getAuthorizationUrlBase();

	
}
