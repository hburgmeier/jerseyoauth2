package com.github.hburgmeier.jerseyoauth2.api.protocol;

public class OAuth2Exception extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OAuth2Exception() {
		super();
	}

	public OAuth2Exception(String message, Throwable cause) {
		super(message, cause);
	}

	public OAuth2Exception(String message) {
		super(message);
	}

	public OAuth2Exception(Throwable cause) {
		super(cause);
	}

}
