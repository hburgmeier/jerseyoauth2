package com.github.hburgmeier.jerseyoauth2.authsrv.api;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractConfiguration implements IConfiguration {
	
	@Override
	public boolean getStrictSecurity() {
		return true;
	}
	
	@Override
	public Set<String> getDefaultScopes() {
		return Collections.emptySet();
	}
	
	@Override
	public boolean getEnableRefreshTokenGeneration() {
		return true;
	}
	
	@Override
	public boolean getAllowScopeEnhancementWithRefreshToken() {
		return false;
	}
	
	@Override
	public boolean getGenerateSecretForPublicClients() {
		return false;
	}
}
