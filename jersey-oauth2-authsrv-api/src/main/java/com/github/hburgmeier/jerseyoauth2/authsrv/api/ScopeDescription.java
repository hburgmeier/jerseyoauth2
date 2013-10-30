package com.github.hburgmeier.jerseyoauth2.authsrv.api;

import java.io.Serializable;

public class ScopeDescription implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String scope;
	private String description;
	
	public ScopeDescription()
	{
		
	}

	public ScopeDescription(String scope, String description) {
		super();
		this.scope = scope;
		this.description = description;
	}

	public String getScope() {
		return scope;
	}

	public String getDescription() {
		return description;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
