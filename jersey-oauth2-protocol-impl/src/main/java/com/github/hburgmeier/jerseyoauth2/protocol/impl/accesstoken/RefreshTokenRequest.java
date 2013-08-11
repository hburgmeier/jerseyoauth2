package com.github.hburgmeier.jerseyoauth2.protocol.impl.accesstoken;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IRefreshTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2Exception;
import com.github.hburgmeier.jerseyoauth2.api.types.GrantType;

public class RefreshTokenRequest extends AbstractTokenRequest implements IRefreshTokenRequest {
	
	private final GrantType grantType;
	private final String refreshToken;
	private final Set<String> scopes;
	
	public RefreshTokenRequest(GrantType grantType, String refreshToken, Set<String> scopes) {
		super();
		this.grantType = grantType;
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
	public void validate() throws OAuth2Exception
	{
		if (grantType == null)
			throw new OAuth2Exception();
		if (StringUtils.isEmpty(refreshToken))
			throw new OAuth2Exception();
	}

	
}
