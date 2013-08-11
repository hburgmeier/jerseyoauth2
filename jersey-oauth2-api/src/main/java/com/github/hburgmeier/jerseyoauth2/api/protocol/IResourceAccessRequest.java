package com.github.hburgmeier.jerseyoauth2.api.protocol;

import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;

public interface IResourceAccessRequest {

	String getAccessToken();
	
	TokenType getTokenType();
}
