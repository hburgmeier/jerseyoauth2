package com.burgmeier.jerseyoauth2.testsuite.services;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;

import com.google.inject.Provider;

public class CacheManagerProvider implements Provider<CacheManager> {

	private CacheManager cacheManager;

	public CacheManagerProvider()
	{
		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		CacheConfiguration tokenCacheConfiguration = new CacheConfiguration().
				maxEntriesLocalHeap(4000).
				name("tokenCache").
				persistence(new PersistenceConfiguration().strategy(Strategy.NONE));
		tokenCacheConfiguration.validateConfiguration();
		config.addCache(tokenCacheConfiguration );
		cacheManager = CacheManager.create(config);
	}
	
	@Override
	public CacheManager get() {
		return cacheManager;
	}

}
