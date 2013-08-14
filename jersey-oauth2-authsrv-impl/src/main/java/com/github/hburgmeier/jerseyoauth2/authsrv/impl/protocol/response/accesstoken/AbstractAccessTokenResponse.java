package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.AbstractOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ResponseFormat;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.ScopeParser;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.oauth2.Constants;

public abstract class AbstractAccessTokenResponse extends AbstractOAuth2Response {
	
	protected final ScopeParser scopeParser = new ScopeParser();
	
	protected Map<String, Object> tokenInfo = new HashMap<>();
	
	public AbstractAccessTokenResponse(int statusCode, ResponseFormat responseFormat, IAccessTokenInfo accessToken, String state) {
		super(statusCode, responseFormat);
		
		tokenInfo.put(Constants.ACCESS_TOKEN, accessToken.getAccessToken());
		if (StringUtils.isNotEmpty(accessToken.getExpiresIn())) {
				tokenInfo.put(Constants.EXPIRES_IN, Long.valueOf(accessToken.getExpiresIn()));
		}
		if (StringUtils.isNotEmpty(accessToken.getRefreshToken())) {		
			tokenInfo.put(Constants.REFRESH_TOKEN, accessToken.getRefreshToken());
		}
		if (accessToken.getTokenType()!=null) {
			tokenInfo.put(Constants.TOKEN_TYPE, accessToken.getTokenType().getTechnicalCode());
		}
		if (accessToken.getAuthorizedScopes()!=null && accessToken.getAuthorizedScopes().isEmpty()) {
			tokenInfo.put(Constants.SCOPE, scopeParser.render(accessToken.getAuthorizedScopes()));
		}
		if (StringUtils.isNotEmpty(state)) {
			tokenInfo.put(Constants.STATE, state);
		}
		
	}
}
