package com.burgmeier.jerseyoauth2.impl.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.ParameterStyle;
import org.apache.amber.oauth2.rs.request.OAuthAccessResourceRequest;

import com.burgmeier.jerseyoauth2.api.IConfiguration;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenVerifier;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.impl.context.OAuthPrincipal;
import com.burgmeier.jerseyoauth2.impl.context.OAuthSecurityContext;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class OAuth20AuthenticationRequestFilter implements ContainerRequestFilter {

	private Set<String> requiredScopes;
	private final IAccessTokenVerifier accessTokenVerifier;
	private ParameterStyle[] parameterStyles;
	
	public OAuth20AuthenticationRequestFilter(final IAccessTokenVerifier accessTokenVerifier, final IConfiguration configuration) {
		this.accessTokenVerifier = accessTokenVerifier;
		this.parameterStyles = configuration.getSupportedOAuthParameterStyles();
	}

	@Override
	public ContainerRequest filter(ContainerRequest containerRequest) {
	
		try {
			OAuthAccessResourceRequest oauthRequest = new 
			        OAuthAccessResourceRequest(new WebRequestAdapter(containerRequest), parameterStyles);
			
			String accessToken = oauthRequest.getAccessToken();
			IAccessTokenInfo accessTokenInfo = accessTokenVerifier.verifyAccessToken(accessToken);
			
			if (requiredScopes!=null)
			{
				if (!matchScopes(requiredScopes, accessTokenInfo.getAuthorizedScopes()))
				{
					throw new WebApplicationException(buildScopeProblem());
				}
			}
			
			OAuthPrincipal principal = new OAuthPrincipal(accessTokenInfo.getClientApp(), accessTokenInfo.getUser(), accessTokenInfo.getAuthorizedScopes());
			SecurityContext securityContext = new OAuthSecurityContext(principal);
			containerRequest.setSecurityContext(securityContext );
			
			return containerRequest;
		} catch (OAuthSystemException e) {
			throw new WebApplicationException(buildAuthProblem());
		} catch (OAuthProblemException e) {
			throw new WebApplicationException(buildAuthProblem());			
		} catch (InvalidTokenException e) {
			throw new WebApplicationException(buildAuthProblem());			
		}
	}
	
	void setRequiredScopes(String[] scopes) {
		this.requiredScopes = new HashSet<>(Arrays.asList(scopes));
	}
	
	protected boolean matchScopes(Set<String> requiredScopes, Set<String> actualScopes)
	{
		if (actualScopes==null && requiredScopes==null)
			return true;
		if (actualScopes==null && requiredScopes!=null && !requiredScopes.isEmpty())
			return false;
		return actualScopes.containsAll(requiredScopes);
	}
	
	protected Response buildScopeProblem()
	{
		return Response.serverError().
			status(401).
			entity("Not allowed").
			build();
	}
	
	private Response buildAuthProblem() {
		return Response.serverError().
				status(401).
				entity("Not allowed").
				build();
	}

}
