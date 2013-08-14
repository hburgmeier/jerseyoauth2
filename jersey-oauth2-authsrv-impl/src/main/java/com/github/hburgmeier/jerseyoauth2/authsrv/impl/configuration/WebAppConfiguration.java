package com.github.hburgmeier.jerseyoauth2.authsrv.impl.configuration;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ScopeDescription;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.ScopeParser;

public class WebAppConfiguration implements IConfiguration {

	private static final long DFEAULT_TOKEN_EXPIRATION = 3600;

	private final ScopeParser scopeParser = new ScopeParser();
	
	private final long tokenExpiration;
	private final Set<String> defaultScopes;
	private final boolean strictSecurity;
	private final boolean supportAuthorizationHeader;
	private final boolean refreshTokenGeneration;

	@Inject
	public WebAppConfiguration(final ServletContext servletContext) {
		this.tokenExpiration = parseLong(servletContext.getInitParameter("oauth2.tokenexpiration"), DFEAULT_TOKEN_EXPIRATION);

		this.defaultScopes = scopeParser.parseScope(servletContext.getInitParameter("oauth2.defaultscopes"));

		this.strictSecurity = parseBoolean(servletContext.getInitParameter("oauth2.strictSecurity"), true);
		this.supportAuthorizationHeader = parseBoolean(servletContext.getInitParameter("oauth2.supportAuthzHeader"),
				true);
		this.refreshTokenGeneration = parseBoolean(servletContext.getInitParameter("oauth2.refreshTokenGeneration"),
				true);
	}

	@Override
	public long getTokenExpiration() {
		return tokenExpiration;
	}

	@Override
	public Map<String, ScopeDescription> getScopeDescriptions() {
		return null;
	}

	@Override
	public Set<String> getDefaultScopes() {
		return defaultScopes;
	}

	@Override
	public boolean getStrictSecurity() {
		return strictSecurity;
	}

	@Override
	public boolean getEnableAuthorizationHeaderForClientAuth() {
		return supportAuthorizationHeader;
	}
	
	@Override
	public boolean getEnableRefreshTokenGeneration() {
		return refreshTokenGeneration;
	}

	private long parseLong(String initParameter, long defaultValue) {
		return initParameter == null ? defaultValue : Long.valueOf(initParameter);
	}

	private boolean parseBoolean(String initParameter, boolean defaultValue) {
		return initParameter == null ? defaultValue : Boolean.parseBoolean(initParameter);
	}

}
