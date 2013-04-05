package com.burgmeier.jerseyoauth2.rs.impl.base;

import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.rs.request.OAuthAccessResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.burgmeier.jerseyoauth2.rs.impl.base.context.OAuthPrincipal;
import com.burgmeier.jerseyoauth2.rs.impl.base.context.OAuthSecurityContext;

public abstract class AbstractOAuth2Filter {

	private static final Logger logger = LoggerFactory.getLogger(AbstractOAuth2Filter.class);	
	
	protected final SecurityContext filterOAuth2Request(OAuthAccessResourceRequest oauthRequest, Set<String> requiredScopes, boolean secureRequest) throws InvalidTokenException, OAuthSystemException
	{
		String accessToken = oauthRequest.getAccessToken();

		IAccessTokenInfo accessTokenInfo = getAccessTokenVerifier().verifyAccessToken(accessToken);
		if (accessTokenInfo==null)
		{
			throw new InvalidTokenException(accessToken);
		}
		if (accessTokenInfo.getUser()==null)
		{
			logger.error("no user stored in token {}", accessToken);
			throw new WebApplicationException(buildUserProblem());
		}
		
		if (accessTokenInfo.getClientApp()==null)
		{
			logger.error("no client stored in token {}", accessToken);
			throw new WebApplicationException(buildClientProblem());
		}
		
		Set<String> authorizedScopes = accessTokenInfo.getAuthorizedScopes();
		if (requiredScopes!=null)
		{
			if (!matchScopes(requiredScopes, authorizedScopes))
			{
				logger.error("Scopes did not match, required {}, actual {}", requiredScopes, authorizedScopes);
				throw new WebApplicationException(buildScopeProblem());
			}
		}
		
		OAuthPrincipal principal = new OAuthPrincipal(accessTokenInfo.getClientApp(), accessTokenInfo.getUser(), authorizedScopes);
		SecurityContext securityContext = new OAuthSecurityContext(principal, secureRequest);	
		return securityContext;
	}
	
	protected abstract IAccessTokenVerifier getAccessTokenVerifier();
	
	protected final boolean matchScopes(Set<String> requiredScopes, Set<String> actualScopes)
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
	
	protected Response buildUserProblem()
	{
		return Response.serverError().
			status(401).
			entity("No authorized user").
			build();
	}	
	
	protected Response buildClientProblem()
	{
		return Response.serverError().
			status(401).
			entity("No authorized client").
			build();
	}	
}
