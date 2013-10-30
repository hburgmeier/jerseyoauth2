package com.github.hburgmeier.jerseyoauth2.authsrv.api;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.Duration;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientType;

public interface IConfiguration {

	Duration getTokenLifetime();
	
	Map<String, ScopeDescription> getScopeDescriptions();
	
	Set<String> getDefaultScopes();

	boolean getStrictSecurity();

	boolean getEnableAuthorizationHeaderForClientAuth();
	
	boolean getEnableRefreshTokenGeneration();
	
	boolean getAllowScopeEnhancementWithRefreshToken();
	
	boolean getGenerateSecretForPublicClients();
	
	EnumSet<ClientType> getAllowedClientTypesForImplicitGrant();
	
	EnumSet<ClientType> getAllowedClientTypesForAuthorizationCode();
}
