package com.burgmeier.jerseyoauth2.api.client;

public interface IClientAuthorization {

	String getCode();

	String getRedirectUrl();
	
	IAuthorizedClientApp getAuthorizedClient();

}
