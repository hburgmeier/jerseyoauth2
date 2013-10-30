package com.github.hburgmeier.jerseyoauth2.authsrv.api.token;

public class TokenGenerationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenGenerationException() {
		super();
	}

	public TokenGenerationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TokenGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenGenerationException(String message) {
		super(message);
	}

	public TokenGenerationException(Throwable cause) {
		super(cause);
	}

}
