package com.burgmeier.jerseyoauth2.api;

import java.util.Map;

public interface IConfiguration {

	long getTokenExpiration();
	
	Map<String, ScopeDescription> getScopeDescriptions();
	
}
