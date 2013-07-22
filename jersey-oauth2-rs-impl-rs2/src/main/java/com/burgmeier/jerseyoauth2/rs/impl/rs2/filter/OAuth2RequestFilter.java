package com.burgmeier.jerseyoauth2.rs.impl.rs2.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2RequestFilter.class);	

	private Set<String> requiredScopes;
	private final IRSConfiguration config;
	private final IAccessTokenVerifier tokenVerifier;	
	
	public OAuth2RequestFilter(String[] requiredScopes, final IRSConfiguration config, final IAccessTokenVerifier tokenVerifier) {
		this.config = config;
		this.tokenVerifier = tokenVerifier;
		this.requiredScopes = new HashSet<String>(Arrays.asList(requiredScopes));
	}

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		if (this.requiredScopes!=null)
		{
			try {
				OAuthAccessResourceRequest oauthRequest = new 
				        OAuthAccessResourceRequest(new WebRequestAdapter(ctx), getRSConfiguration().getSupportedOAuthParameterStyles());
				LOGGER.debug("parse request successful");		
				
				boolean secure = isRequestSecure(ctx.getUriInfo().getRequestUri(), ctx.getHeaderString("X-SSL-Secure")); 
				
				SecurityContext securityContext = filterOAuth2Request(oauthRequest, requiredScopes, secure);
				ctx.setSecurityContext(securityContext);
			} catch (OAuthSystemException e) {
				LOGGER.error("Error in filter request", e);
				ctx.abortWith(buildAuthProblem());
			} catch (OAuthProblemException e) {
				LOGGER.error("Error in filter request", e);
				ctx.abortWith(buildAuthProblem());			
			} catch (InvalidTokenException e) {
				LOGGER.error("Error in filter request", e);
				ctx.abortWith(buildAuthProblem());			
			} catch (OAuth2FilterException e) {
				LOGGER.debug("Filtered Request", e);
				ctx.abortWith(e.getErrorResponse());
			}
		}
	}

	@Override
	protected IAccessTokenVerifier getAccessTokenVerifier() {
		return tokenVerifier;
	}
	
	protected IRSConfiguration getRSConfiguration()
	{
		return config;
	}	


}
