package com.burgmeier.jerseyoauth2.impl.filter;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.burgmeier.jerseyoauth2.api.IConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Provider
public class ConfigurationServiceProvider implements ContextResolver<IConfiguration>{

	private final IConfiguration configuration;

	@Inject
	public ConfigurationServiceProvider(final IConfiguration configuration)
	{
		this.configuration = configuration;
	}
	
	@Override
	public IConfiguration getContext(Class<?> type) {
		return this.configuration;
	}

}