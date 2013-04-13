package com.burgmeier.jerseyoauth2.rs.impl.rs2.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.rs.request.OAuthAccessResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.burgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.burgmeier.jerseyoauth2.rs.impl.base.AbstractOAuth2Filter;
import com.burgmeier.jerseyoauth2.rs.impl.base.OAuth2FilterException;

@Provider
public class OAuth2RequestFilter extends AbstractOAuth2Filter implements ContainerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(OAuth2RequestFilter.class);	
	
	@Context
	private Providers providers;

	private Set<String> requiredScopes;	
	
	
	public OAuth2RequestFilter(String[] requiredScopes) {
		this.requiredScopes = new HashSet<String>(Arrays.asList(requiredScopes));
	}

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		try {
			OAuthAccessResourceRequest oauthRequest = new 
			        OAuthAccessResourceRequest(new WebRequestAdapter(ctx), getRSConfiguration().getSupportedOAuthParameterStyles());
			logger.debug("parse request successful");		
			
			boolean secure = isRequestSecure(ctx.getUriInfo().getRequestUri(), ctx.getHeaderString("X-SSL-Secure")); 
			
			SecurityContext securityContext = filterOAuth2Request(oauthRequest, requiredScopes, secure);
			ctx.setSecurityContext(securityContext);
		} catch (OAuthSystemException e) {
			logger.error("Error in filter request", e);
			ctx.abortWith(buildAuthProblem());
		} catch (OAuthProblemException e) {
			logger.error("Error in filter request", e);
			ctx.abortWith(buildAuthProblem());			
		} catch (InvalidTokenException e) {
			logger.error("Error in filter request", e);
			ctx.abortWith(buildAuthProblem());			
		} catch (OAuth2FilterException e) {
			logger.debug("Filtered Request", e);
			ctx.abortWith(e.getErrorResponse());
		}
	}

	@Override
	protected IAccessTokenVerifier getAccessTokenVerifier() {
		return providers.getContextResolver(IAccessTokenVerifier.class, MediaType.WILDCARD_TYPE).getContext(IAccessTokenVerifier.class);
	}
	
	protected IRSConfiguration getRSConfiguration()
	{
		return providers.getContextResolver(IRSConfiguration.class, MediaType.WILDCARD_TYPE).getContext(IRSConfiguration.class);
	}	


}
