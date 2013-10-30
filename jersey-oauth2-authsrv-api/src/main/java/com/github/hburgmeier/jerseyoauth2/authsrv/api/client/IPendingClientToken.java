package com.github.hburgmeier.jerseyoauth2.authsrv.api.client;


public interface IPendingClientToken {

	static final long TEN_MINUTES = 10*60*1000;
	
	String getCode();

	IAuthorizedClientApp getAuthorizedClient();

	boolean isExpired();

}
