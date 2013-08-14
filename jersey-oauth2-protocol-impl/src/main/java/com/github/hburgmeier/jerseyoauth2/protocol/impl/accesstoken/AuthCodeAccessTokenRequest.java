package com.github.hburgmeier.jerseyoauth2.protocol.impl.accesstoken;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthCodeAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.types.GrantType;

public class AuthCodeAccessTokenRequest extends AbstractTokenRequest implements IAuthCodeAccessTokenRequest {

	private final GrantType grantType;
	private final String code;
	private final String clientId;
	private final String clientSecret;
	private final String redirectUri;
	
	public AuthCodeAccessTokenRequest(GrantType grantType, String clientId, String clientSecret, String code, String redirectUri) {
		super();
		this.grantType = grantType;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.code = code;
		this.redirectUri = redirectUri;
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
	public void validate() throws OAuth2ParseException
	{
		if (grantType == null) {
			throw new OAuth2ParseException("Missing grant_type", null);
		}
		if (StringUtils.isEmpty(code)) {
			throw new OAuth2ParseException("Missing code", null);
		}
		if (StringUtils.isEmpty(redirectUri)) {
			throw new OAuth2ParseException("Missing redirect uri", null);
		}
		if (StringUtils.isEmpty(clientId)) {
			throw new OAuth2ParseException("Missing client id", null);
		}
		
	}

}
