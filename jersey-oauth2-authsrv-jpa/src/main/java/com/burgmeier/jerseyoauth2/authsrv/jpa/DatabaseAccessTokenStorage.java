package com.burgmeier.jerseyoauth2.authsrv.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;

public class DatabaseAccessTokenStorage implements IAccessTokenStorageService {

	private EntityManagerFactory emf;

	public DatabaseAccessTokenStorage()
	{
		emf = Persistence.createEntityManagerFactory("authsrv");		
	}
	
	@Override
	public IAccessTokenInfo getTokenInfoByAccessToken(String accessToken) throws InvalidTokenException {
		EntityManager em = emf.createEntityManager();
		TokenEntity tokenEntity = em.find(TokenEntity.class, accessToken);
		return tokenEntity;
	}

	@Override
	public IAccessTokenInfo issueToken(String accessToken, String refreshToken, IAuthorizedClientApp clientApp) {
		EntityManager em = emf.createEntityManager();
		TokenEntity te = new TokenEntity(accessToken, refreshToken, clientApp);
		saveTokenEntity(em, te);
		return te;
	}

	@Override
	public IAccessTokenInfo getTokenInfoByRefreshToken(String refreshToken) throws InvalidTokenException {
		EntityManager em = emf.createEntityManager();
		TypedQuery<TokenEntity> query = em.createNamedQuery("findTokenEntityByRefreshToken", TokenEntity.class);
		query.setParameter("refreshToken", refreshToken);
		TokenEntity te = query.getSingleResult();
		return te;
	}

	@Override
	public IAccessTokenInfo refreshToken(String oldAccessToken, String newAccessToken, String newRefreshToken) {
		EntityManager em = emf.createEntityManager();
		TokenEntity tokenEntity = em.find(TokenEntity.class, oldAccessToken);
		tokenEntity.updateTokens(newAccessToken, newRefreshToken);
		saveTokenEntity(em, tokenEntity);
		return tokenEntity;
	}

	protected void saveTokenEntity(EntityManager em, TokenEntity te) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(te);
			tx.commit();
		} catch (PersistenceException ex) {
			tx.rollback();
			throw ex;
		}
	}
	
	
}
