package com.burgmeier.jerseyoauth2.authsrv.api.client;


public interface IRegisteredClientApp {

	String getClientId();
	
	String getClientSecret();
	
	String getApplicationName();

	String getCallbackUrl();
	
	ClientType getClientType();
}
