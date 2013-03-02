package com.burgmeier.jerseyoauth2.authsrv.api.client;

public class ClientServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientServiceException() {
		super();
	}

	public ClientServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientServiceException(String message) {
		super(message);
	}

	public ClientServiceException(Throwable cause) {
		super(cause);
	}

}
