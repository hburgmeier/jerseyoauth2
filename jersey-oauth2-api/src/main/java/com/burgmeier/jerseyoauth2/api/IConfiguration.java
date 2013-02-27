package com.burgmeier.jerseyoauth2.api;

import java.util.Map;
import java.util.Set;

public interface IConfiguration {

	long getTokenExpiration();
	
	Map<String, ScopeDescription> getScopeDescriptions();
	
	Set<String> getDefaultScopes();
	
}
