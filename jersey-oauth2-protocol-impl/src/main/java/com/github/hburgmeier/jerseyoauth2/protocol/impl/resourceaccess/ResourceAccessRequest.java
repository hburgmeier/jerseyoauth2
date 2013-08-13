package com.github.hburgmeier.jerseyoauth2.protocol.impl.resourceaccess;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;

public class ResourceAccessRequest implements IResourceAccessRequest {

	protected String accessToken;
	protected final TokenType tokenType;
	
	public ResourceAccessRequest(String accessToken, TokenType tokenType) {
		super();
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}
	
	public void validate() throws OAuth2ParseException
	{
		if (StringUtils.isEmpty(accessToken))
			throw new OAuth2ParseException("Missing token", null);
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}
	
	@Override
	public TokenType getTokenType() {
		return tokenType;
	}
}
