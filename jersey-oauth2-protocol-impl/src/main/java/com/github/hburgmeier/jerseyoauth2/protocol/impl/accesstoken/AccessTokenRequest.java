package com.github.hburgmeier.jerseyoauth2.protocol.impl.accesstoken;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2Exception;
import com.github.hburgmeier.jerseyoauth2.api.types.GrantType;

public class AccessTokenRequest implements IAccessTokenRequest {

	private final GrantType grantType;
	private final String code;
	private final String clientId;
	private final String clientSecret;
	private final String redirectUri;
	private final String refreshToken;
	private final Set<String> scopes;
	
	public AccessTokenRequest(GrantType grantType, String clientId, String clientSecret, String code, String redirectUri, String refreshToken, Set<String> scopes) {
		super();
		this.grantType = grantType;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.code = code;
		this.redirectUri = redirectUri;
		this.refreshToken = refreshToken;
		this.scopes = scopes;
	}

	@Override
	public GrantType getGrantType() {
		return grantType;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getRedirectUri() {
		return redirectUri;
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
	public String getRefreshToken() {
		return refreshToken;
	}
	
	@Override
	public Set<String> getScopes() {
		return scopes;
	}
	
	public void validate() throws OAuth2Exception
	{
		if (grantType == null)
			throw new OAuth2Exception();
		if (grantType == GrantType.AUTHORIZATION_REQUEST ||
			grantType == GrantType.IMPLICIT_GRANT)
		{
			if (StringUtils.isEmpty(code))
				throw new OAuth2Exception();
			if (StringUtils.isEmpty(redirectUri))
				throw new OAuth2Exception();
			if (StringUtils.isEmpty(clientId))
				throw new OAuth2Exception();
		}
		if (grantType == GrantType.REFRESH_TOKEN)
		{
			if (StringUtils.isEmpty(refreshToken))
				throw new OAuth2Exception();
		}
		
	}

}
