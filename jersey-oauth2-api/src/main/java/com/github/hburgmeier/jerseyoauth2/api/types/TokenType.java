package com.github.hburgmeier.jerseyoauth2.api.types;

public enum TokenType {

	BEARER,
	MAC;
	
	public String getTechnicalCode()
	{
		return this.name().toLowerCase();
	}
}
