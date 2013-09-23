package com.github.hburgmeier.jerseyoauth2.api.protocol;

public class OAuth2ProtocolException extends OAuth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final OAuth2ErrorCode errorCode;
	private final String description;
	private final String errorUri;
	private final String state;

	public OAuth2ProtocolException(OAuth2ErrorCode errorCode, String description, String errorUri, String state, Throwable cause)
	{
		super(description, cause);
		this.errorCode = errorCode;
		this.description = description;
		this.errorUri = errorUri;
		this.state = state;
	}
	
	public OAuth2ProtocolException(OAuth2ErrorCode errorCode, String state)
	{
		this(errorCode, null, null, state, null);
	}
	
	public OAuth2ProtocolException(OAuth2ErrorCode errorCode, String description, String state)
	{
		this(errorCode, description, null, state, null);
	}
	
	public OAuth2ProtocolException(OAuth2ErrorCode errorCode, String description, String state, Throwable cause)
	{
		this(errorCode, description, null, state, cause);
	}

	public OAuth2ErrorCode getErrorCode() {
		return errorCode;
	}

	public String getDescription() {
		return description;
	}

	public String getState() {
		return state;
	}

	public String getErrorUri() {
		return errorUri;
	}
	
	
}
