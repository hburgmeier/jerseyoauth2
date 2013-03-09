package com.burgmeier.jerseyoauth2.authsrv.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.google.inject.Inject;

public class DatabaseAccessTokenStorage implements IAccessTokenStorageService {

	private EntityManagerFactory emf;
	private IConfiguration config;

	@Inject
	public DatabaseAccessTokenStorage(final EntityManagerFactory emf, final IConfiguration config)
	{
		this.emf = emf;
		this.config = config;
	}
	
	@Override
	public IAccessTokenInfo getTokenInfoByAccessToken(String accessToken) throws InvalidTokenException {
		EntityManager em = emf.createEntityManager();		
		TokenEntity tokenEntity = em.find(TokenEntity.class, accessToken);
		setUser(tokenEntity);
		return tokenEntity;
	}

	@Override
	public IAccessTokenInfo issueToken(String accessToken, String refreshToken, IAuthorizedClientApp clientApp) {
		assert(clientApp instanceof AuthorizedClientApplication);
		
		TokenEntity te = new TokenEntity(accessToken, refreshToken, (AuthorizedClientApplication)clientApp, config.getTokenExpiration());
		saveTokenEntity(te);
		return te;
	}

	@Override
	public IAccessTokenInfo getTokenInfoByRefreshToken(String refreshToken) throws InvalidTokenException {
		EntityManager em = emf.createEntityManager();
		TypedQuery<TokenEntity> query = em.createNamedQuery("findTokenEntityByRefreshToken", TokenEntity.class);
		query.setParameter("refreshToken", refreshToken);
		TokenEntity te = query.getSingleResult();
		setUser(te);
		return te;
	}

	@Override
	public IAccessTokenInfo refreshToken(String oldAccessToken, String newAccessToken, String newRefreshToken) {
		EntityManager em = emf.createEntityManager();
		TokenEntity tokenEntity = em.find(TokenEntity.class, oldAccessToken);
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(tokenEntity);
			em.flush();
			tx.commit();
		} catch (PersistenceException ex) {
			tx.rollback();
			throw ex;
		} finally {
			em.close();
		}
		
		tokenEntity.updateTokens(newAccessToken, newRefreshToken);
		saveTokenEntity(tokenEntity);
		return tokenEntity;
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
			tx.rollback();
			throw ex;
		} finally {
			em.close();
		}
	}
	
	protected void setUser(TokenEntity tokenEntity) {
		if (tokenEntity!=null)
		{
			AuthorizedClientApplication clientApp = (AuthorizedClientApplication) tokenEntity.getClientApp();
			clientApp.setAuthorizedUser(new User(clientApp.getUserName()));
		}
	}
	
}
