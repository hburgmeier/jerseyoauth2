package com.burgmeier.jerseyoauth2.api.token;

public class InvalidTokenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -102890656134366078L;

	public InvalidTokenException() {
	}

	public InvalidTokenException(String arg0) {
		super(arg0);
	}

	public InvalidTokenException(Throwable arg0) {
		super(arg0);
	}

	public InvalidTokenException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
