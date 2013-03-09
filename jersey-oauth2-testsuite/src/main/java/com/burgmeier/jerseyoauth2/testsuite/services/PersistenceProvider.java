package com.burgmeier.jerseyoauth2.testsuite.services;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.inject.Provider;

public class PersistenceProvider implements Provider<EntityManagerFactory> {

	private EntityManagerFactory emf;

	public PersistenceProvider()
	{
		emf = Persistence.createEntityManagerFactory("authsrv");
	}
	
	@Override
	public EntityManagerFactory get() {
		return emf;
	}

}
