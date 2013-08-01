package com.github.hburgmeier.jerseyoauth2.rs.impl.rs2.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2Exception;
import com.github.hburgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.github.hburgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.github.hburgmeier.jerseyoauth2.rs.impl.base.AbstractOAuth2Filter;
import com.github.hburgmeier.jerseyoauth2.rs.impl.base.OAuth2FilterException;

@Provider
public class OAuth2RequestFilter extends AbstractOAuth2Filter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2RequestFilter.class);	

	private Set<String> requiredScopes;
	private final IAccessTokenVerifier tokenVerifier;	
	private final IRequestFactory requestFactory;

	private final EnumSet<ParameterStyle> parameterStyles;
	private final EnumSet<TokenType> tokenTypes;
	
	public OAuth2RequestFilter(String[] requiredScopes, final IRSConfiguration config, final IAccessTokenVerifier tokenVerifier, final IRequestFactory requestFactory) {
		this.tokenVerifier = tokenVerifier;
		this.requestFactory = requestFactory;
		this.requiredScopes = new HashSet<String>(Arrays.asList(requiredScopes));
		parameterStyles = config.getSupportedOAuthParameterStyles();
		tokenTypes = config.getSupportedTokenTypes();
	}

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		if (this.requiredScopes!=null)
		{
			try {
				IResourceAccessRequest oauthRequest = requestFactory.parseResourceAccessRequest(new HttpRequestAdapter(ctx), 
						parameterStyles, tokenTypes); 
				LOGGER.debug("parse request successful");		
				
				boolean secure = isRequestSecure(ctx.getUriInfo().getRequestUri(), ctx.getHeaderString("X-SSL-Secure")); 
				
				SecurityContext securityContext = filterOAuth2Request(oauthRequest, requiredScopes, secure);
				ctx.setSecurityContext(securityContext);
			} catch (OAuth2Exception e) {
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

}
