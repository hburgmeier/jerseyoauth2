package com.burgmeier.jerseyoauth2.testsuite.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.amber.oauth2.common.message.types.ParameterStyle;

import com.burgmeier.jerseyoauth2.api.IConfiguration;
import com.burgmeier.jerseyoauth2.api.ScopeDescription;

public class Configuration implements IConfiguration {

	private Map<String, ScopeDescription> scopeDescriptions = new HashMap<String, ScopeDescription>();
	private Set<String> defaultScope = new HashSet<>();
	
	public Configuration()
	{
		scopeDescriptions.put("test1", new ScopeDescription("test1", "Test Scope 1"));
		scopeDescriptions.put("test2", new ScopeDescription("test2", "Test Scope 2"));
		defaultScope.add("defaultScope");
	}
	
	@Override
	public long getTokenExpiration() {
		return 3600;
	}

	@Override
	public Map<String, ScopeDescription> getScopeDescriptions() {
		return Collections.unmodifiableMap(scopeDescriptions);
	}

	@Override
	public Set<String> getDefaultScopes() {
		return defaultScope;
	}

	@Override
	public ParameterStyle[] getSupportedOAuthParameterStyles() {
		return new ParameterStyle[]{ ParameterStyle.QUERY, ParameterStyle.HEADER };
	}
	
	

}
