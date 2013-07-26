package com.github.hburgmeier.jerseyoauth2.api.types;

public enum ResponseType {

	CODE("code"),
	TOKEN("token");
	
	private String technicalCode;

	ResponseType(String technicalCode)
	{
		this.technicalCode = technicalCode;
	}
	
	public String getTechnicalCode() {
		return technicalCode;
	}
}
