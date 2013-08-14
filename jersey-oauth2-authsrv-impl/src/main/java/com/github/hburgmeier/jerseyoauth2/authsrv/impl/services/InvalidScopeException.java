package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

public class InvalidScopeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String scope;

	public InvalidScopeException(String scope)
	{
		super();
		this.scope = scope;
	}

	public String getScope() {
		return scope;
	}
}
