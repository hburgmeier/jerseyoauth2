package com.burgmeier.jerseyoauth2.api.simple;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.client.IClientAuthorization;

public class SimpleClientAuthorization implements IClientAuthorization {

	private String code;
	private String redirectUrl;
	private IAuthorizedClientApp authorizedClient;
	
	public SimpleClientAuthorization(String code, String redirectUrl, IAuthorizedClientApp authorizedClient) {
		super();
		this.code = code;
		this.redirectUrl = redirectUrl;
		this.authorizedClient = authorizedClient;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getRedirectUrl() {
		return redirectUrl;
	}

	@Override
	public IAuthorizedClientApp getAuthorizedClient() {
		return authorizedClient;
	}

}
