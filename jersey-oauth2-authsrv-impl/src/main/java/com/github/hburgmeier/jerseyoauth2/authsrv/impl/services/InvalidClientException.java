package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

public class InvalidClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidClientException() {
		super();
	}

	public InvalidClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidClientException(String message) {
		super(message);
	}

	public InvalidClientException(Throwable cause) {
		super(cause);
	}

}
