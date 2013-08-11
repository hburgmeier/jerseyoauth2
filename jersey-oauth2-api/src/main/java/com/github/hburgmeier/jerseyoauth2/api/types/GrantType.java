package com.github.hburgmeier.jerseyoauth2.api.types;

import java.util.EnumSet;

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
	
	public static GrantType parse(String technicalCode)
	{
		for (GrantType type : EnumSet.allOf(GrantType.class))
		{
			if (type.getTechnicalCode().equals(technicalCode.toLowerCase()))
				return type;
		}
		throw new IllegalArgumentException(technicalCode);
	}
}
