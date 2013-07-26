package com.github.hburgmeier.jerseyoauth2.authsrv.api.client;

import com.github.hburgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;

public interface IPendingClientToken {

	String getCode();

	IAuthorizedClientApp getAuthorizedClient();

}
