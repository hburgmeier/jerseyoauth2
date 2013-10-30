package com.github.hburgmeier.jerseyoauth2.api.protocol;

public class OAuth2ParseException extends OAuth2ProtocolException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OAuth2ParseException(String description, String state)
	{
		super(OAuth2ErrorCode.INVALID_REQUEST, description, state);
	}
	
}
