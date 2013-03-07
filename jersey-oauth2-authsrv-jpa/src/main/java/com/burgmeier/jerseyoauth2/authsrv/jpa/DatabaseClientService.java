package com.burgmeier.jerseyoauth2.authsrv.jpa;

import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientType;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientAuthorization;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;

public class DatabaseClientService implements IClientService {

	private EntityManagerFactory emf;

	public DatabaseClientService()
	{
		emf = Persistence.createEntityManagerFactory("authsrv");
	}
	
	@Override
	public IRegisteredClientApp registerClient(String appName, String callbackUrl) throws ClientServiceException {
		String clientId = UUID.randomUUID().toString();
		String clientSecret = UUID.randomUUID().toString();
		
		RegisteredClient client = new RegisteredClient(clientId, clientSecret);
		client.setApplicationName(appName);
		client.setCallbackUrl(callbackUrl);
		EntityManager entityManager = emf.createEntityManager();
		entityManager.persist(client);
		return client;
	}

	@Override
	public IRegisteredClientApp getRegisteredClient(String clientId) {
		EntityManager entityManager = emf.createEntityManager();
		RegisteredClient client = entityManager.find(RegisteredClient.class, clientId);
		return client;
	}

	@Override
	public IAuthorizedClientApp findPendingClient(String clientId, String clientSecret, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAuthorizedClientApp authorizeClient(IUser user, IRegisteredClientApp clientApp, Set<String> allowedScopes)
			throws ClientServiceException {
		AuthorizedClientApplication clApp = new AuthorizedClientApplication();
		EntityManager entityManager = emf.createEntityManager();
		entityManager.persist(clApp);
		return clApp;
	}

	@Override
	public IClientAuthorization isAuthorized(IUser user, String clientId, Set<String> scopes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IClientAuthorization createClientAuthorization(IAuthorizedClientApp clientApp) throws ClientServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
