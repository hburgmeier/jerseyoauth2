package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

public class InvalidRedirectUrlException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRedirectUrlException() {
		super();
	}

	public InvalidRedirectUrlException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRedirectUrlException(String message) {
		super(message);
	}

	public InvalidRedirectUrlException(Throwable cause) {
		super(cause);
	}

}
