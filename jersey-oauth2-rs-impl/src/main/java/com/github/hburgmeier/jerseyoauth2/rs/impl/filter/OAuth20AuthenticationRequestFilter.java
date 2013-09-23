package com.github.hburgmeier.jerseyoauth2.rs.impl.filter;

import java.net.URI;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.github.hburgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.github.hburgmeier.jerseyoauth2.rs.impl.base.AbstractOAuth2Filter;
import com.github.hburgmeier.jerseyoauth2.rs.impl.base.OAuth2FilterException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

class OAuth20AuthenticationRequestFilter extends AbstractOAuth2Filter implements ContainerRequestFilter {

	private static final String X_SSL_SECURE = "X-SSL-Secure";
	private static final String ERROR_FILTER_REQUEST = "Error in filter request";

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth20AuthenticationRequestFilter.class);
	
	private Set<String> requiredScopes;
	private final IAccessTokenVerifier accessTokenVerifier;
	private final IRequestFactory requestFactory;
	private EnumSet<ParameterStyle> parameterStyles;
	private EnumSet<TokenType> tokenTypes;
	
	public OAuth20AuthenticationRequestFilter(final IAccessTokenVerifier accessTokenVerifier, final IRSConfiguration configuration, 
			final IRequestFactory requestFactory) {
		this.accessTokenVerifier = accessTokenVerifier;
		this.requestFactory = requestFactory;
		this.parameterStyles = configuration.getSupportedOAuthParameterStyles();
		this.tokenTypes = configuration.getSupportedTokenTypes();
	}

	@Override
	public ContainerRequest filter(ContainerRequest containerRequest) {
	
		try {
			IResourceAccessRequest oauthRequest = requestFactory.parseResourceAccessRequest(new HttpRequestAdapter(containerRequest), parameterStyles, tokenTypes);
			LOGGER.debug("parse request successful");

			URI requestUri = containerRequest.getRequestUri();
			String secureSSL = containerRequest.getHeaderValue(X_SSL_SECURE);			
			boolean secure = isRequestSecure(requestUri, secureSSL);
			SecurityContext securityContext = filterOAuth2Request(oauthRequest, requiredScopes, secure);
			
			containerRequest.setSecurityContext(securityContext );
			LOGGER.debug("set SecurityContext. User {}", securityContext.getUserPrincipal().getName());
			
			return containerRequest;
		} catch (OAuth2ParseException e) {
			LOGGER.debug(ERROR_FILTER_REQUEST, e);
			throw new WebApplicationException(e, buildAuthProblem());			
		} catch (InvalidTokenException e) {
			LOGGER.error(ERROR_FILTER_REQUEST, e);
			throw new WebApplicationException(e, buildAuthProblem());			
		} catch (OAuth2FilterException e) {
			LOGGER.error(ERROR_FILTER_REQUEST, e);
			throw new WebApplicationException(e, e.getErrorResponse());			
		}
	}

	void setRequiredScopes(String[] scopes) {
		this.requiredScopes = new HashSet<>(Arrays.asList(scopes));
	}

	@Override
	protected IAccessTokenVerifier getAccessTokenVerifier() {
		return accessTokenVerifier;
	}

}
