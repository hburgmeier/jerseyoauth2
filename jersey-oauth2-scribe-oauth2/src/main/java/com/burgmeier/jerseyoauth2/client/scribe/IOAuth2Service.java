package com.burgmeier.jerseyoauth2.client.scribe;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public interface IOAuth2Service extends OAuthService {

	Token refreshToken(OAuth2Token token);

}
