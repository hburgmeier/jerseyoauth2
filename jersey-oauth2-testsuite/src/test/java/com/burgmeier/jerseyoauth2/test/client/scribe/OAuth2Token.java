package com.burgmeier.jerseyoauth2.test.client.scribe;

import org.scribe.model.Token;

public class OAuth2Token extends Token {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String refreshToken;
	private String expiration;

	public OAuth2Token(String token, String refreshToken, String expiration, String rawResponse) {
		super(token, "", rawResponse);
		this.refreshToken = refreshToken;
		this.expiration = expiration;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public String getExpiration() {
		return expiration;
	}

}
