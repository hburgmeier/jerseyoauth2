package com.github.hburgmeier.jerseyoauth2.api.protocol;

import java.util.Set;

import com.github.hburgmeier.jerseyoauth2.api.types.GrantType;

public interface IAccessTokenRequest {

	GrantType getGrantType();
	
	String getCode();
	
	String getRedirectUri();
	
	String getClientId();
	
	String getClientSecret();
	
	String getRefreshToken();
	
	Set<String> getScopes();
}
