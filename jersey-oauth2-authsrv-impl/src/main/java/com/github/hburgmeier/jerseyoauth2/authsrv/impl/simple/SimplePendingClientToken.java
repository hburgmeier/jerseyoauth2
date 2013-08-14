package com.github.hburgmeier.jerseyoauth2.authsrv.impl.simple;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IPendingClientToken;

public class SimplePendingClientToken implements IPendingClientToken {

	private String code;
	private IAuthorizedClientApp authorizedClient;
	private final long expiration;
	
	public SimplePendingClientToken(String code, IAuthorizedClientApp authorizedClient) {
		super();
		this.code = code;
		this.authorizedClient = authorizedClient;
		this.expiration = System.currentTimeMillis() + TEN_MINUTES; 
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public IAuthorizedClientApp getAuthorizedClient() {
		return authorizedClient;
	}
	
	@Override
	public boolean isExpired() {
		return System.currentTimeMillis() > this.expiration;
	}
}
