package com.github.hburgmeier.jerseyoauth2.sample.openid;

import java.util.Map;
import java.util.Set;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ScopeDescription;

public class Configuration implements IConfiguration {

	@Override
	public long getTokenExpiration() {
		return 3600;
	}

	@Override
	public Map<String, ScopeDescription> getScopeDescriptions() {
		return null;
	}

	@Override
	public Set<String> getDefaultScopes() {
		return null;
	}

	@Override
	public boolean getStrictSecurity() {
		return false;
	}

	@Override
	public boolean getEnableAuthorizationHeaderForClientAuth() {
		return false;
	}
	
	@Override
	public boolean getEnableRefreshTokenGeneration() {
		return true;
	}
	
	@Override
	public boolean getAllowScopeEnhancementWithRefreshToken() {
		return true;
	}

}
