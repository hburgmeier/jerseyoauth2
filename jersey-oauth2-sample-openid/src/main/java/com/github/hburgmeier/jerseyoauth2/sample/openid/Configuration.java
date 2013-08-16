package com.github.hburgmeier.jerseyoauth2.sample.openid;

import java.util.Map;

import org.joda.time.Duration;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.AbstractConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ScopeDescription;

public class Configuration extends AbstractConfiguration {

	@Override
	public Duration getTokenLifetime() {
		return Duration.standardHours(1);
	}

	@Override
	public Map<String, ScopeDescription> getScopeDescriptions() {
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
	public boolean getAllowScopeEnhancementWithRefreshToken() {
		return true;
	}

}
