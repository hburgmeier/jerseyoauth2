package com.github.hburgmeier.jerseyoauth2.rs.impl.guice;

import javax.inject.Inject;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.google.inject.Singleton;

@Singleton
@Provider
public class RequestFactoryProvider  implements ContextResolver<IRequestFactory>{

	protected IRequestFactory requestFactory;

	@Inject
	public RequestFactoryProvider(final IRequestFactory requestFactory)
	{
		this.requestFactory = requestFactory;
	}
	
	@Override
	public IRequestFactory getContext(Class<?> type) {
		return this.requestFactory;
	}
}