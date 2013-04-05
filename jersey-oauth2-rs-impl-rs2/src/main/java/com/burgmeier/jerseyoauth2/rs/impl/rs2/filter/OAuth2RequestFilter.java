package com.burgmeier.jerseyoauth2.rs.impl.rs2.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.burgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.burgmeier.jerseyoauth2.rs.impl.base.AbstractOAuth2Filter;

@Provider
public class OAuth2RequestFilter extends AbstractOAuth2Filter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		
	}

	@Override
	protected IAccessTokenVerifier getAccessTokenVerifier() {
		// TODO Auto-generated method stub
		return null;
	}


}
