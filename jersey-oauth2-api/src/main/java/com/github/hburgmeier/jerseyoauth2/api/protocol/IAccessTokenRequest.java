package com.github.hburgmeier.jerseyoauth2.api.protocol;

import com.github.hburgmeier.jerseyoauth2.api.types.GrantType;

public interface IAccessTokenRequest {

	GrantType getGrantType();

	String getClientId();
	
	String getClientSecret();
}
