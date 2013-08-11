package com.github.hburgmeier.jerseyoauth2.protocol.impl.resourceaccess;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;

public class ResourceAccessRequest implements IResourceAccessRequest {

	protected String accessToken;
	protected final TokenType tokenType;
	
	public ResourceAccessRequest(String accessToken, TokenType tokenType) {
		super();
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}
	
	public void validate()
	{
		//TODO
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
