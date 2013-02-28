package com.burgmeier.jerseyoauth2.api;

import java.util.Map;
import java.util.Set;

import org.apache.amber.oauth2.common.message.types.ParameterStyle;

public interface IConfiguration {

	long getTokenExpiration();
	
	Map<String, ScopeDescription> getScopeDescriptions();
	
	Set<String> getDefaultScopes();

	ParameterStyle[] getSupportedOAuthParameterStyles();
	
}
