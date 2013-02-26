package com.burgmeier.jerseyoauth2.api.simple;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.client.IClientAuthorization;

public class SimpleClientAuthorization implements IClientAuthorization {

	private String code;
	private IAuthorizedClientApp authorizedClient;
	
	public SimpleClientAuthorization(String code, IAuthorizedClientApp authorizedClient) {
		super();
		this.code = code;
		this.authorizedClient = authorizedClient;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public IAuthorizedClientApp getAuthorizedClient() {
		return authorizedClient;
	}

}
