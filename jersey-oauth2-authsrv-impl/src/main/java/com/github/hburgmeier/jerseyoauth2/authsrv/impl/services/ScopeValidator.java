package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

import java.util.Map;
import java.util.Set;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ScopeDescription;

public class ScopeValidator {

	protected final IConfiguration configuration;
	
	public ScopeValidator(IConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public void validateScopes(Set<String> scopes) throws InvalidScopeException
	{
		Map<String, ScopeDescription> scopeDescriptions = configuration.getScopeDescriptions();
		for (String scope : scopes)
		{
			if (!scopeDescriptions.containsKey(scope))
			{
				throw new InvalidScopeException(scope);
			}
		}
	}

	public boolean isScopeEqual(Set<String> scopes1, Set<String> scopes2) {
		return scopes1.containsAll(scopes2) &&
				scopes2.containsAll(scopes1);
	}
	
}
