package com.github.hburgmeier.jerseyoauth2.rs.impl.base;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Set;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.github.hburgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.github.hburgmeier.jerseyoauth2.rs.impl.base.context.OAuthPrincipal;
import com.github.hburgmeier.jerseyoauth2.rs.impl.base.context.OAuthSecurityContext;

public abstract class AbstractOAuth2Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOAuth2Filter.class);
	
	private static final String HTTPS = "https";
	
	protected final SecurityContext filterOAuth2Request(IResourceAccessRequest oauthRequest, Set<String> requiredScopes, boolean secureRequest) throws InvalidTokenException, OAuth2FilterException
	{
		String accessToken = oauthRequest.getAccessToken();

		IAccessTokenInfo accessTokenInfo = getAccessTokenVerifier().verifyAccessToken(accessToken);
		if (accessTokenInfo==null)
		{
			throw new InvalidTokenException(accessToken);
		}
		if (accessTokenInfo.getUser()==null)
		{
			LOGGER.error("no user stored in token {}", accessToken);
			throw new OAuth2FilterException(buildUserProblem());
		}
		
		if (accessTokenInfo.getClientApp()==null)
		{
			LOGGER.error("no client stored in token {}", accessToken);
			throw new OAuth2FilterException(buildClientProblem());
		}
		
		Set<String> authorizedScopes = accessTokenInfo.getAuthorizedScopes();
		if (requiredScopes!=null)
		{
			if (!matchScopes(requiredScopes, authorizedScopes))
			{
				LOGGER.error("Scopes did not match, required {}, actual {}", requiredScopes, authorizedScopes);
				throw new OAuth2FilterException(buildScopeProblem());
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
			status(HttpURLConnection.HTTP_UNAUTHORIZED).
			entity("Not allowed").
			build();
	}
	
	protected Response buildUserProblem()
	{
		return Response.serverError().
			status(HttpURLConnection.HTTP_UNAUTHORIZED).
			entity("No authorized user").
			build();
	}	
	
	protected Response buildClientProblem()
	{
		return Response.serverError().
			status(HttpURLConnection.HTTP_UNAUTHORIZED).
			entity("No authorized client").
			build();
	}	
	
	protected Response buildAuthProblem() {
		return Response.serverError().
				status(HttpURLConnection.HTTP_UNAUTHORIZED).
				entity("Not allowed").
				build();
	}	
	
	protected boolean isRequestSecure(URI requestUri, String secureSSL) {
		if (secureSSL!=null && "true".equals(secureSSL))
			return true;
		String scheme = requestUri.getScheme();
		return scheme!=null?scheme.equalsIgnoreCase(HTTPS):false;
	}	
}
