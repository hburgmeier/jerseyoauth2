package com.github.hburgmeier.jerseyoauth2.rs.impl.base;

import javax.ws.rs.core.Response;

public class OAuth2FilterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Response errorResponse;

	public OAuth2FilterException() {
		super();
	}

	public OAuth2FilterException(Response errorResponse) {
		super();
		this.setErrorResponse(errorResponse);
		
	}

	public Response getErrorResponse() {
		return errorResponse;
	}

	public final void setErrorResponse(Response errorResponse) {
		this.errorResponse = errorResponse;
	}

}
