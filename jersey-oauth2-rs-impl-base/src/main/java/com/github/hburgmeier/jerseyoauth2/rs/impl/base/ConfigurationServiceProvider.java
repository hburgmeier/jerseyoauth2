package com.github.hburgmeier.jerseyoauth2.rs.impl.base;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.github.hburgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Provider
public class ConfigurationServiceProvider implements ContextResolver<IRSConfiguration>{

	private final IRSConfiguration configuration;

	@Inject
	public ConfigurationServiceProvider(final IRSConfiguration configuration)
	{
		this.configuration = configuration;
	}
	
	@Override
	public IRSConfiguration getContext(Class<?> type) {
		return this.configuration;
	}

}