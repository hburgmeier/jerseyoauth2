package com.github.hburgmeier.jerseyoauth2.rs.impl.filter;

import com.github.hburgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.github.hburgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

class OAuth20AuthenticationFilter implements ResourceFilter {

	private final OAuth20AuthenticationRequestFilter requestFilter;
	
	public OAuth20AuthenticationFilter(final IAccessTokenVerifier accessTokenVerifier, final IRSConfiguration configuration)
	{
		requestFilter = new OAuth20AuthenticationRequestFilter(accessTokenVerifier, configuration);
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
