package com.github.hburgmeier.jerseyoauth2.authsrv.api;

import java.util.Map;
import java.util.Set;

public interface IConfiguration {

	long getTokenExpiration();
	
	Map<String, ScopeDescription> getScopeDescriptions();
	
	Set<String> getDefaultScopes();

	boolean getStrictSecurity();

	boolean getEnableAuthorizationHeaderForClientAuth();
	
}
