package com.github.hburgmeier.jerseyoauth2.api.types;

public enum GrantType {

	AUTHORIZATION_REQUEST("authorization_code"),
	IMPLICIT_GRANT("password"),
	CLIENT_CREDENTIALS("client_credentials"),
	REFRESH_TOKEN("refresh_token");
	
	private String technicalCode;

	private GrantType(String technicalCode) {
		this.technicalCode = technicalCode;
	}
	
	public String getTechnicalCode() {
		return technicalCode;
	}
}
