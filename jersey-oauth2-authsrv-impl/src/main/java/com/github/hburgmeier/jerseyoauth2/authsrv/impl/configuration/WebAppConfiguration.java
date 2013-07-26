package com.github.hburgmeier.jerseyoauth2.authsrv.impl.configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ScopeDescription;
import com.google.inject.Inject;

public class WebAppConfiguration implements IConfiguration {

	private final long tokenExpiration;
	private final Set<String> defaultScopes;
	private final boolean strictSecurity;
	private final boolean supportAuthorizationHeader;

	@Inject
	public WebAppConfiguration(final ServletContext servletContext) {
		this.tokenExpiration = parseLong(servletContext.getInitParameter("oauth2.tokenexpiration"), 3600);

		this.defaultScopes = parseScopes(servletContext.getInitParameter("oauth2.defaultscopes"));

		this.strictSecurity = parseBoolean(servletContext.getInitParameter("oauth2.strictSecurity"), true);
		this.supportAuthorizationHeader = parseBoolean(servletContext.getInitParameter("oauth2.supportAuthzHeader"),
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
	public boolean getSupportAuthorizationHeader() {
		return supportAuthorizationHeader;
	}

	private Set<String> parseScopes(String initParameter) {
		Set<String> scopes = new HashSet<>();
		if (initParameter != null) {
			StringTokenizer st = new StringTokenizer(initParameter, ",");
			while (st.hasMoreTokens())
				scopes.add(st.nextToken());
		}
		return scopes;
	}

	private long parseLong(String initParameter, long defaultValue) {
		return initParameter == null ? defaultValue : Long.valueOf(initParameter);
	}

	private boolean parseBoolean(String initParameter, boolean defaultValue) {
		return initParameter == null ? defaultValue : Boolean.parseBoolean(initParameter);
	}

}
