package com.github.hburgmeier.jerseyoauth2.api.protocol;

public interface IAuthCodeAccessTokenRequest extends IAccessTokenRequest {
	
	String getCode();
	
	String getRedirectUri();
	
	String getClientId();
	
	String getClientSecret();

}
