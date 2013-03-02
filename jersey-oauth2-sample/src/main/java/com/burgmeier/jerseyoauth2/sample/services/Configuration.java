package com.burgmeier.jerseyoauth2.sample.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.amber.oauth2.common.message.types.ParameterStyle;

import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.ScopeDescription;

public class Configuration implements IConfiguration {

	private Set<String> defaultScopes;
	private Map<String, ScopeDescription> scopeDescriptions = new HashMap<>();

	public Configuration()
	{
		this.defaultScopes = new HashSet<String>(Arrays.asList(ScopeConstants.RANDOM));
		
		ScopeDescription sd = new ScopeDescription(ScopeConstants.RANDOM, "Access to Random Generator");
		scopeDescriptions.put(ScopeConstants.RANDOM, sd);
	}
	
	@Override
	public long getTokenExpiration() {
		return 3600l;
	}

	@Override
	public Map<String, ScopeDescription> getScopeDescriptions() {
		return scopeDescriptions;
	}

	@Override
	public Set<String> getDefaultScopes() {
		return defaultScopes;
	}

	@Override
	public ParameterStyle[] getSupportedOAuthParameterStyles() {
		return new ParameterStyle[]{ ParameterStyle.QUERY, ParameterStyle.HEADER };
	}

}
