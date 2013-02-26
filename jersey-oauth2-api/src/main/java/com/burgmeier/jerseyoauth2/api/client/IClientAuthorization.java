package com.burgmeier.jerseyoauth2.api.client;

public interface IClientAuthorization {

	String getCode();

	IAuthorizedClientApp getAuthorizedClient();

}
