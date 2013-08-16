package com.github.hburgmeier.jerseyoauth2.protocol.impl.accesstoken;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IRefreshTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.types.GrantType;

public class RefreshTokenRequest extends AbstractTokenRequest implements IRefreshTokenRequest {
	
	private final GrantType grantType;
	private final String refreshToken;
	private final Set<String> scopes;
	private final String clientId;
	private final String clientSecret;
	
	public RefreshTokenRequest(GrantType grantType, String clientId, String clientSecret, String refreshToken, Set<String> scopes) {
		super();
		this.grantType = grantType;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.refreshToken = refreshToken;
		this.scopes = scopes;
	}

	@Override
	public GrantType getGrantType() {
		return grantType;
	}
	
	@Override
	public String getRefreshToken() {
		return refreshToken;
	}
	
	@Override
	public Set<String> getScopes() {
		return scopes;
	}
	
	@Override
	public String getClientId() {
		return clientId;
	}
	
	@Override
	public String getClientSecret() {
		return clientSecret;
	}
	
	@Override
	public void validate() throws OAuth2ParseException
	{
		if (grantType == null) {
			throw new OAuth2ParseException("Missing grant_type", null);
		}
		if (StringUtils.isEmpty(refreshToken)) {
			throw new OAuth2ParseException("Missing refresh token", null);
		}
	}

	
}
