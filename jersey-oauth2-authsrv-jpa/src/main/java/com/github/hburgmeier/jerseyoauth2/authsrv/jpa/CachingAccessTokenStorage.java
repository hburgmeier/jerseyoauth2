package com.github.hburgmeier.jerseyoauth2.authsrv.jpa;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.github.hburgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.TokenStorageException;

public class CachingAccessTokenStorage implements IAccessTokenStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CachingAccessTokenStorage.class);
	
	protected final DatabaseAccessTokenStorage delegate;
	protected final Ehcache tokenCache;

	@Inject
	public CachingAccessTokenStorage(EntityManagerFactory emf, IConfiguration config, CacheManager cacheManager) {
		this.delegate = new DatabaseAccessTokenStorage(emf, config);
		tokenCache = cacheManager.getEhcache("tokenCache");
		assert(tokenCache!=null);
	}

	@Override
	public IAccessTokenInfo getTokenInfoByAccessToken(String accessToken) throws InvalidTokenException {
		Element element = tokenCache.get(accessToken);
		if (element != null &&
				!element.isExpired()) {
			IAccessTokenInfo token = (IAccessTokenInfo) element.getObjectValue();
			if (!token.isExpired())
			{
				return token;
			}
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
		LOGGER.debug("token refreshed");
		return newToken;
	}

	@Override
	public List<IAccessTokenInfo> invalidateTokensForUser(String username) {
		List<IAccessTokenInfo> tokens = delegate.invalidateTokensForUser(username);
		for (IAccessTokenInfo token : tokens)
		{
			tokenCache.remove(token.getAccessToken());
			LOGGER.debug("token invalidated");
		}
		return tokens;
	}

}
