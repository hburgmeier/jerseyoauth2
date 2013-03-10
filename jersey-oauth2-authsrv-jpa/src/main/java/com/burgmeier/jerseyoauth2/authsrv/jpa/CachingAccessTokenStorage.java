package com.burgmeier.jerseyoauth2.authsrv.jpa;

import javax.persistence.EntityManagerFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.burgmeier.jerseyoauth2.authsrv.api.token.TokenStorageException;
import com.google.inject.Inject;

public class CachingAccessTokenStorage implements IAccessTokenStorageService {

	protected final DatabaseAccessTokenStorage delegate;
	protected final Ehcache tokenCache;

	@Inject
	public CachingAccessTokenStorage(final EntityManagerFactory emf, final IConfiguration config, final CacheManager cacheManager) {
		this.delegate = new DatabaseAccessTokenStorage(emf, config);
		tokenCache = cacheManager.getEhcache("tokenCache");
		assert(tokenCache!=null);
	}

	@Override
	public IAccessTokenInfo getTokenInfoByAccessToken(String accessToken) throws InvalidTokenException {
		Element element;
		if ((element = tokenCache.get(accessToken)) != null &&
				!element.isExpired()) {
			IAccessTokenInfo token = (IAccessTokenInfo) element.getObjectValue();
			return token;
		}
		IAccessTokenInfo token = delegate.getTokenInfoByAccessToken(accessToken);
		if (token!=null)
		{
			tokenCache.put(new Element(accessToken, token));
		}
		return token;
	}

	@Override
	public IAccessTokenInfo issueToken(String accessToken, String refreshToken, IAuthorizedClientApp clientApp) {
		IAccessTokenInfo token = delegate.issueToken(accessToken, refreshToken, clientApp);
		tokenCache.put(new Element(accessToken, token));
		return token;
	}

	@Override
	public IAccessTokenInfo getTokenInfoByRefreshToken(String refreshToken) throws InvalidTokenException {
		return delegate.getTokenInfoByRefreshToken(refreshToken);
	}

	@Override
	public IAccessTokenInfo refreshToken(String oldAccessToken, String newAccessToken, String newRefreshToken) throws TokenStorageException {
		tokenCache.remove(oldAccessToken);
		IAccessTokenInfo newToken = delegate.refreshToken(oldAccessToken, newAccessToken, newRefreshToken);
		tokenCache.put(new Element(newAccessToken, newToken));
		return newToken;
	}

}
