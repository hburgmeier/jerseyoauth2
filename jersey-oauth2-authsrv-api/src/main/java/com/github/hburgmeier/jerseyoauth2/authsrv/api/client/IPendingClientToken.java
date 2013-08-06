package com.github.hburgmeier.jerseyoauth2.authsrv.api.client;


public interface IPendingClientToken {

	String getCode();

	IAuthorizedClientApp getAuthorizedClient();

}
