package com.burgmeier.jerseyoauth2.test.client.scribe;

public class TokenExtractorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenExtractorException() {
	}

	public TokenExtractorException(String message) {
		super(message);
	}

	public TokenExtractorException(Throwable cause) {
		super(cause);
	}

	public TokenExtractorException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenExtractorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
