package com.burgmeier.jerseyoauth2.impl.filter;

import com.burgmeier.jerseyoauth2.api.IConfiguration;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenService;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class OAuth20AuthenticationFilter implements ResourceFilter {

	private final OAuth20AuthenticationRequestFilter requestFilter;
	
	public OAuth20AuthenticationFilter(final IAccessTokenService accessTokenService, final IConfiguration configuration)
	{
		requestFilter = new OAuth20AuthenticationRequestFilter(accessTokenService, configuration);
	}
	
	@Override
	public ContainerRequestFilter getRequestFilter() {
		return requestFilter;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return null;
	}

	public void setRequiredScopes(String[] scopes) {
		requestFilter.setRequiredScopes(scopes);
	}

}
