package com.github.hburgmeier.jerseyoauth2.authsrv.jpa;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.TokenStorageException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.IUserStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.UserStorageServiceException;
import com.google.inject.Inject;

public class DatabaseAccessTokenStorage implements IAccessTokenStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseAccessTokenStorage.class);
	
	private EntityManagerFactory emf;
	private IConfiguration config;
	
	@Inject(optional=true)
	private IUserStorageService userStorageService = null;

	@Inject
	public DatabaseAccessTokenStorage(final EntityManagerFactory emf, final IConfiguration config)
	{
		this.emf = emf;
		this.config = config;
	}
	
	@Override
	public IAccessTokenInfo getTokenInfoByAccessToken(String accessToken) throws InvalidTokenException {
		try {
			EntityManager em = emf.createEntityManager();
			try {
				TokenEntity tokenEntity = em.find(TokenEntity.class, accessToken);
				if (tokenEntity!=null && !tokenEntity.isExpired())
				{
					setUser(tokenEntity);
					return tokenEntity;
				} else {
					if (tokenEntity==null)
						LOGGER.debug("token {} unknown", accessToken);
					else  {
						removeToken(em, tokenEntity);
						LOGGER.debug("token {} expired", accessToken);
					}
					return null;
				}
			} finally {
				em.close();
			}
		} catch (UserStorageServiceException e) {
			throw new InvalidTokenException(e);
		}
	}

	@Override
	public IAccessTokenInfo issueToken(String accessToken, String refreshToken, IAuthorizedClientApp clientApp) {
		assert(clientApp instanceof AuthorizedClientApplication);
		
		long validUntil = System.currentTimeMillis()+(config.getTokenExpiration()*1000l);
		TokenEntity te = new TokenEntity(accessToken, refreshToken, (AuthorizedClientApplication)clientApp, config.getTokenExpiration(), TokenType.BEARER, validUntil);
		saveTokenEntity(te);
		LOGGER.debug("token {} saved", accessToken);
		return te;
	}

	@Override
	public IAccessTokenInfo getTokenInfoByRefreshToken(String refreshToken) throws InvalidTokenException {
		try {
			EntityManager em = emf.createEntityManager();
			try {
				TypedQuery<TokenEntity> query = em.createNamedQuery("findTokenEntityByRefreshToken", TokenEntity.class);
				query.setParameter("refreshToken", refreshToken);
				TokenEntity te = query.getSingleResult();
				if (!te.isExpired())
				{
					setUser(te);
					return te;
				} else {
					LOGGER.debug("refresh token {} is expired", refreshToken);
					throw new InvalidTokenException("expired");
				}
			} finally {
				em.close();
			}
		} catch (NoResultException e) {
			throw new InvalidTokenException(e);
		} catch (UserStorageServiceException e) {
			throw new InvalidTokenException(e);
		}
	}

	@Override
	public IAccessTokenInfo refreshToken(String oldAccessToken, String newAccessToken, String newRefreshToken) throws TokenStorageException {
		try {
			EntityManager em = emf.createEntityManager();
			try {
				TokenEntity tokenEntity = em.find(TokenEntity.class, oldAccessToken);
				removeToken(em, tokenEntity);
				
				tokenEntity.updateTokens(newAccessToken, newRefreshToken);
				saveTokenEntity(tokenEntity);
				setUser(tokenEntity);
				return tokenEntity;
			} finally {
				em.close();
			}
		} catch (UserStorageServiceException | PersistenceException e) {
			throw new TokenStorageException(e);
		}
	}

	protected void removeToken(EntityManager em, TokenEntity tokenEntity) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(tokenEntity);
			em.flush();
			tx.commit();
		} catch (PersistenceException ex) {
			LOGGER.error("persistence error", ex);
			tx.rollback();
			throw ex;
		}
	}

	@Override
	public List<IAccessTokenInfo> invalidateTokensForUser(IUser user) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<TokenEntity> query = em.createNamedQuery("findTokenEntityByUsername", TokenEntity.class);
		query.setParameter("username", user.getName());
		List<TokenEntity> resultList = query.getResultList();
		List<IAccessTokenInfo> result = new LinkedList<>();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			for (TokenEntity te : resultList)
			{
				em.remove(te);
				result.add(te);
			}
			em.flush();
			tx.commit();
			LOGGER.debug("tokens for user {} invalidated", user.getName());
		} catch (PersistenceException ex) {
			LOGGER.error("persistence error", ex);
			tx.rollback();
			throw ex;
		} finally {
			em.close();
		}
		return result;
	}
	
	protected void saveTokenEntity(TokenEntity te) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(te);
			em.flush();
			tx.commit();
		} catch (PersistenceException ex) {
			LOGGER.error("persistence error", ex);
			tx.rollback();
			throw ex;
		} finally {
			em.close();
		}
	}
	
	protected void setUser(TokenEntity tokenEntity) throws UserStorageServiceException {
		if (tokenEntity!=null)
		{
			AuthorizedClientApplication clientApp = (AuthorizedClientApplication) tokenEntity.getClientApp();
			if (userStorageService!=null)
			{
				LOGGER.debug("using UserStorageService");
				IUser iUser = userStorageService.loadUser(clientApp.getUsername());
				clientApp.setAuthorizedUser(iUser);
			} else {
				LOGGER.debug("using no UserStorageService");
				clientApp.setAuthorizedUser(new User(clientApp.getUsername()));
			}
		}
	}
	
}
