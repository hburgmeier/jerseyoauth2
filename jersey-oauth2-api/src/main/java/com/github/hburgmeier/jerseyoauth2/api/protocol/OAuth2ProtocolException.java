package com.github.hburgmeier.jerseyoauth2.api.protocol;

public class OAuth2ProtocolException extends OAuth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final OAuth2ErrorCode errorCode;
	private String description;
	private String uri;
	private String state;

	public OAuth2ProtocolException(OAuth2ErrorCode errorCode, String description, String uri, String state)
	{
		this.errorCode = errorCode;
		this.description = description;
		this.uri = uri;
		this.state = state;
	}
	
	public OAuth2ProtocolException(OAuth2ErrorCode errorCode, String state)
	{
		this(errorCode, null, null, state);
	}
	
	public OAuth2ProtocolException(OAuth2ErrorCode errorCode, String description, String state)
	{
		this(errorCode, description, null, state);
	}
	
	
}
