package com.github.hburgmeier.jerseyoauth2.api.protocol;

public enum OAuth2ErrorCode {

	INVALID_REQUEST("invalid_request"),
	INVALID_GRANT("invalid_grant"),
	INVALID_CLIENT("invalid_client"),
	UNAUTHORIZED_CLIENT("unauthorized_client"),
	ACCESS_DENIED("access_denied"),
	UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type"),
	UNSUPPORTED_GRANT_TYPE("unsupported_grant_type"),
	INVALID_SCOPE("invalid_scope"),
	SERVER_ERROR("server_error"),
	TEMPORARILY_UNAVAILABLE("temporarily_unavailable");
	
	private final String technicalCode;
	
	private OAuth2ErrorCode(String technicalCode) {
		this.technicalCode = technicalCode;
	}

	public String getTechnicalCode()
	{
		return technicalCode;
	}
}
