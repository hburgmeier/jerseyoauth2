package com.burgmeier.jerseyoauth2.authsrv.impl.simple;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IPendingClientToken;

public class SimpleClientAuthorization implements IPendingClientToken {

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
