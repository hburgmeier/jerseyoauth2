package com.burgmeier.jerseyoauth2.rs.impl.filter;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Providers;

import com.burgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.burgmeier.jerseyoauth2.rs.api.annotations.AllowedScopes;
import com.burgmeier.jerseyoauth2.rs.api.annotations.OAuth20;
import com.burgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
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
			else {
				oauth20 = am.getResource().getAnnotation(OAuth20.class);
				scopes = am.getResource().getAnnotation(AllowedScopes.class);
				if (oauth20!=null)
				{
					return getFilters(scopes);				
				}
				return null;	
			}
		} else
			return null;
	}
	
	protected List<ResourceFilter> getFilters(AllowedScopes scopes)
	{
		List<ResourceFilter> securityFilters = new LinkedList<ResourceFilter>();
		OAuth20AuthenticationFilter oAuth20AuthenticationFilter = new OAuth20AuthenticationFilter(getAccessTokenVerifier(), getRSConfiguration());
		if (scopes!=null && scopes.scopes().length>0)
		{
			oAuth20AuthenticationFilter.setRequiredScopes(scopes.scopes());
		}
		securityFilters.add(oAuth20AuthenticationFilter);
		return securityFilters;
	}
	
	protected IAccessTokenVerifier getAccessTokenVerifier()
	{
		return providers.getContextResolver(IAccessTokenVerifier.class, MediaType.WILDCARD_TYPE).getContext(IAccessTokenVerifier.class);
	}
	
	protected IRSConfiguration getRSConfiguration()
	{
		return providers.getContextResolver(IRSConfiguration.class, MediaType.WILDCARD_TYPE).getContext(IRSConfiguration.class);
	}	
	
}