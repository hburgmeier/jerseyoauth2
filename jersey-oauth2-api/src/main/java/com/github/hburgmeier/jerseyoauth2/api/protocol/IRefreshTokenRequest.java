package com.github.hburgmeier.jerseyoauth2.api.protocol;

import java.util.Set;

public interface IRefreshTokenRequest extends IAccessTokenRequest {
	
	String getRefreshToken();
	
	Set<String> getScopes();

}
