package com.burgmeier.jerseyoauth2.authsrv.jpa;

import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientType;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IPendingClientToken;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.burgmeier.jerseyoauth2.authsrv.api.user.IUserStorageService;
import com.burgmeier.jerseyoauth2.authsrv.api.user.UserStorageServiceException;

public class DatabaseClientService implements IClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseClientService.class);
	
	private final EntityManagerFactory emf;
	
	@com.google.inject.Inject(optional=true)
	private IUserStorageService userStorageService = null;

	@Inject
	public DatabaseClientService(EntityManagerFactory emf)
	{
		this.emf = emf;
	}
	
	@Override
	public IRegisteredClientApp registerClient(String appName, String callbackUrl, ClientType clientType) throws ClientServiceException {
		String clientId = UUID.randomUUID().toString();
		String clientSecret = UUID.randomUUID().toString();

		RegisteredClient client = new RegisteredClient(clientId, clientSecret);
		client.setApplicationName(appName);
		client.setCallbackUrl(callbackUrl);
		client.setClientType(clientType);
		persist(client);
		LOGGER.debug("registered client {}", clientId);
		return client;
	}

	@Override
	public IRegisteredClientApp getRegisteredClient(String clientId) {
		EntityManager entityManager = emf.createEntityManager();
		RegisteredClient client = entityManager.find(RegisteredClient.class, clientId);
		return client;
	}

	@Override
	public IAuthorizedClientApp authorizeClient(IUser user, IRegisteredClientApp clientApp, Set<String> allowedScopes)
			throws ClientServiceException {
		assert(clientApp instanceof RegisteredClient);
		
		AuthorizedClientApplication clApp = new AuthorizedClientApplication((RegisteredClient)clientApp, user, allowedScopes);
		persist(clApp);
		return clApp;
	}

	@Override
	public IAuthorizedClientApp isAuthorized(IUser user, String clientId, Set<String> scopes) {
		EntityManager entityManager = emf.createEntityManager();
		try {
			TypedQuery<AuthorizedClientApplication> query = entityManager.createNamedQuery("findAuthorizedClient", AuthorizedClientApplication.class);
			query.setParameter("username", user.getName());
			query.setParameter("clientId", clientId);
			AuthorizedClientApplication result = query.getSingleResult();
			
			if (!result.getAuthorizedScopes().containsAll(scopes))
			{
				LOGGER.debug("scopes do not match authorized scopes {} auth {}", scopes, result.getAuthorizedScopes());
				return null;
			}
			
			setUser(result);			
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (UserStorageServiceException e) {
			return null;
		} finally {
			entityManager.close();
		}
	}

	@Override
	public IPendingClientToken createPendingClientToken(IAuthorizedClientApp clientApp) throws ClientServiceException {
		assert(clientApp instanceof AuthorizedClientApplication);
		
		PendingClientToken pendingClientAuth = new PendingClientToken((AuthorizedClientApplication) clientApp);
		persist(pendingClientAuth);
		return pendingClientAuth;
	}
	
	@Override
	public IPendingClientToken findPendingClientToken(String clientId, String clientSecret, String code) {
		EntityManager entityManager = emf.createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		try {
			TypedQuery<PendingClientToken> query = entityManager.createNamedQuery("findPendingByCode", PendingClientToken.class);
			query.setParameter("code", code);
			query.setParameter("clientId", clientId);
			query.setParameter("clientSecret", clientSecret);
			PendingClientToken result = query.getSingleResult();
			try {
				tx.begin();
				entityManager.remove(result);
				tx.commit();
			} catch (PersistenceException e) {
				LOGGER.error("persistence error - rollback", e);
				tx.rollback();
				result = null;
			}
			setUser((AuthorizedClientApplication)result.getAuthorizedClient());
			return result;
		} catch (NoResultException e) {
			return null;
		} catch (UserStorageServiceException e) {
			return null;
		} finally {
			entityManager.close();
		}
	}	

	protected void persist(Object obj) {
		EntityManager entityManager = emf.createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			entityManager.persist(obj);
			entityManager.flush();
			tx.commit();
		} catch (PersistenceException e) {
			LOGGER.error("persistence error - rollback", e);
			tx.rollback();
			throw e;
		} finally {
			entityManager.close();
		}
	}
	
	protected void setUser(AuthorizedClientApplication result) throws UserStorageServiceException {
		if (userStorageService!=null)
		{
			LOGGER.debug("using userStorageService to load user");
			IUser iUser = userStorageService.loadUser(result.getUsername());
			result.setAuthorizedUser(iUser);
		}
		else {
			LOGGER.debug("using no user storage service");
			result.setAuthorizedUser(new User(result.getUsername()));
		}
	}
	
}
