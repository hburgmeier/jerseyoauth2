package com.github.hburgmeier.jerseyoauth2.rs.impl.filter;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.github.hburgmeier.jerseyoauth2.rs.api.annotations.AllowedScopes;
import com.github.hburgmeier.jerseyoauth2.rs.api.annotations.OAuth20;
import com.github.hburgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class OAuth20FilterFactory  implements ResourceFilterFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth20FilterFactory.class);
	
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
				LOGGER.debug("Installing oauth2 filter on {}", am.getResource());
				return getFilters(scopes);
			}
			else {
				oauth20 = am.getResource().getAnnotation(OAuth20.class);
				scopes = am.getResource().getAnnotation(AllowedScopes.class);
				if (oauth20!=null)
				{
					LOGGER.debug("Installing oauth2 filter on {}", am.getResource());
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
		OAuth20AuthenticationFilter oAuth20AuthenticationFilter = new OAuth20AuthenticationFilter(getAccessTokenVerifier(), 
				getRSConfiguration(), getRequestFactory());
		if (scopes!=null && scopes.scopes().length>0)
		{
			LOGGER.debug("Installing scope filter");
			oAuth20AuthenticationFilter.setRequiredScopes(scopes.scopes());
		}
		securityFilters.add(oAuth20AuthenticationFilter);
		return securityFilters;
	}
	
	protected IRequestFactory getRequestFactory() {
		return providers.getContextResolver(IRequestFactory.class, MediaType.WILDCARD_TYPE).getContext(IRequestFactory.class);
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