package com.burgmeier.jerseyoauth2.rs.impl.rs2.filter;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import com.burgmeier.jerseyoauth2.rs.api.annotations.AllowedScopes;
import com.burgmeier.jerseyoauth2.rs.api.annotations.OAuth20;

@Provider
public class OAuth2FilterFeature implements DynamicFeature {

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		boolean classAnnotation = resourceInfo.getResourceClass().isAnnotationPresent(OAuth20.class);
		boolean methodAnnotation = resourceInfo.getResourceMethod().isAnnotationPresent(OAuth20.class);
		
		if (classAnnotation || methodAnnotation)
		{
			AllowedScopes scopes = methodAnnotation?resourceInfo.getResourceMethod().getAnnotation(AllowedScopes.class):
				resourceInfo.getResourceClass().getAnnotation(AllowedScopes.class);
			OAuth2RequestFilter filter = new OAuth2RequestFilter(scopes.scopes());
			context.register(filter, 100);
		}
	}

}
