package com.burgmeier.jerseyoauth2.client.scribe;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class OAuth2ServiceWrapper implements IOAuth2Service {

	private OAuthService wrapped;
	private DefaultApi20 api;
	private OAuthConfig config;

	public OAuth2ServiceWrapper(OAuthService wrapped, DefaultApi20 api, OAuthConfig config)
	{
		this.wrapped = wrapped;
		this.api = api;
		this.config = config;
	}

	@Override
	public Token getRequestToken() {
		return wrapped.getRequestToken();
	}

	@Override
	public Token getAccessToken(Token requestToken, Verifier verifier) {
		return wrapped.getAccessToken(requestToken, verifier);
	}

	@Override
	public void signRequest(Token accessToken, OAuthRequest request) {
		wrapped.signRequest(accessToken, request);
	}

	@Override
	public String getVersion() {
		return wrapped.getVersion();
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return wrapped.getAuthorizationUrl(requestToken);
	}

	@Override
	public Token refreshToken(OAuth2Token token) {
	    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
	    request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
	    request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
	    request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
	    request.addQuerystringParameter("refresh_token", token.getRefreshToken());
	    if(config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
	    Response response = request.send();
	    return api.getAccessTokenExtractor().extract(response.getBody());
	}

}
