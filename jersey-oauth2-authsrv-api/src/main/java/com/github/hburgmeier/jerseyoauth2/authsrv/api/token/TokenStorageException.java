package com.github.hburgmeier.jerseyoauth2.authsrv.api.token;

public class TokenStorageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenStorageException() {
	}

	public TokenStorageException(String message) {
		super(message);
	}

	public TokenStorageException(Throwable cause) {
		super(cause);
	}

	public TokenStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenStorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
