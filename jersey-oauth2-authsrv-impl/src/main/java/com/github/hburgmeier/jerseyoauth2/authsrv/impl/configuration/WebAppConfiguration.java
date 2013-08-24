package com.github.hburgmeier.jerseyoauth2.authsrv.impl.configuration;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ScopeDescription;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientType;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.ScopeParser;

public class WebAppConfiguration implements IConfiguration {

	private static final Duration DFEAULT_TOKEN_LIFETIME = Duration.standardDays(1);

	private final ScopeParser scopeParser = new ScopeParser();
	
	private final Duration tokenDuration;
	private final Set<String> defaultScopes;
	private final boolean strictSecurity;
	private final boolean supportAuthorizationHeader;
	private final boolean refreshTokenGeneration;
	private final boolean allowScopeEnhancementWithRefreshToken;
	private final boolean generateSecretForPublicClients;

	@Inject
	public WebAppConfiguration(final ServletContext servletContext) {
		this.tokenDuration = parseDuration(servletContext.getInitParameter("oauth2.tokenexpiration"), DFEAULT_TOKEN_LIFETIME);

		this.defaultScopes = scopeParser.parseScope(servletContext.getInitParameter("oauth2.defaultscopes"));

		this.strictSecurity = parseBoolean(servletContext.getInitParameter("oauth2.strictSecurity"), true);
		this.supportAuthorizationHeader = parseBoolean(servletContext.getInitParameter("oauth2.supportAuthzHeader"),
				true);
		this.refreshTokenGeneration = parseBoolean(servletContext.getInitParameter("oauth2.refreshTokenGeneration"),
				true);
		this.allowScopeEnhancementWithRefreshToken = parseBoolean(servletContext.getInitParameter("oauth2.allowScopeEnhancement"),
				true);
		this.generateSecretForPublicClients = parseBoolean(servletContext.getInitParameter("oauth2.generateSecretForPublicClients"),
				false);
	}

	@Override
	public Duration getTokenLifetime() {
		return tokenDuration;
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

	@Override
	public boolean getAllowScopeEnhancementWithRefreshToken() {
		return allowScopeEnhancementWithRefreshToken;
	}
	
	@Override
	public boolean getGenerateSecretForPublicClients() {
		return generateSecretForPublicClients;
	}

	@Override
	public EnumSet<ClientType> getAllowedClientTypesForImplicitGrant() {
		return EnumSet.allOf(ClientType.class);
	}

	@Override
	public EnumSet<ClientType> getAllowedClientTypesForAuthorizationCode() {
		return EnumSet.of(ClientType.CONFIDENTIAL);
	}	
	
	private boolean parseBoolean(String initParameter, boolean defaultValue) {
		return initParameter == null ? defaultValue : Boolean.parseBoolean(initParameter);
	}
	
	private Duration parseDuration(String initParameter, Duration defaultDuration)
	{
		if (initParameter!=null)
		{
			PeriodFormatter formatter = new PeriodFormatterBuilder()
	    		.appendDays().appendSuffix("d ")
	    		.appendHours().appendSuffix("h ")
	    		.appendMinutes().appendSuffix("min")
	    		.toFormatter();
			Period p = formatter.parsePeriod(initParameter);
			return p.toDurationFrom(DateTime.now());
		} else
			return defaultDuration;
	}

}
