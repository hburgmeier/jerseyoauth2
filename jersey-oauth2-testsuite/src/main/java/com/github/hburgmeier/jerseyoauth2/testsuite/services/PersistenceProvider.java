package com.github.hburgmeier.jerseyoauth2.testsuite.services;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.inject.Provider;

public class PersistenceProvider implements Provider<EntityManagerFactory> {

	private EntityManagerFactory emf;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PersistenceProvider()
	{
		Map params = new HashMap();
		params.put("hibernate.hbm2ddl.auto","create-drop");
		params.put("hibernate.cache.use_second_level_cache","false");
		params.put("hibernate.dialect","org.hibernate.dialect.DerbyTenSevenDialect");
		
		emf = Persistence.createEntityManagerFactory("authsrv", params );
	}
	
	@Override
	public EntityManagerFactory get() {
		return emf;
	}

}
