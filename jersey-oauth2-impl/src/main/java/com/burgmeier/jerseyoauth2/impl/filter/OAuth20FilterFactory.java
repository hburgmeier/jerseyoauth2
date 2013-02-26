package com.burgmeier.jerseyoauth2.impl.filter;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Providers;

import com.burgmeier.jerseyoauth2.api.annotations.AllowedScopes;
import com.burgmeier.jerseyoauth2.api.annotations.OAuth20;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenService;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class OAuth20FilterFactory  implements ResourceFilterFactory {

	@Context
	private Providers providers;
	
	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		if (am instanceof AbstractResourceMethod)
		{
			OAuth20 oauth20 = am.getAnnotation(OAuth20.class);
			AllowedScopes scopes = am.getAnnotation(AllowedScopes.class);
			
			if (oauth20!=null)
			{
				return getFilters(scopes);
			}
			else
				return null;
		} else
			return null;
	}
	
	protected List<ResourceFilter> getFilters(AllowedScopes scopes)
	{
		List<ResourceFilter> securityFilters = new LinkedList<ResourceFilter>();
		OAuth20AuthenticationFilter oAuth20AuthenticationFilter = new OAuth20AuthenticationFilter(getAccessTokenService());
		if (scopes!=null && scopes.scopes().length>0)
		{
			oAuth20AuthenticationFilter.setRequiredScopes(scopes.scopes());
		}
		securityFilters.add(oAuth20AuthenticationFilter);
		return securityFilters;
	}
	
	protected IAccessTokenService getAccessTokenService()
	{
		return providers.getContextResolver(IAccessTokenService.class, MediaType.WILDCARD_TYPE).getContext(IAccessTokenService.class);
	}
	
}