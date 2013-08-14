package com.github.hburgmeier.jerseyoauth2.authsrv.jpa.guice;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;

import com.google.inject.Provider;

public class DefaultCacheManagerProvider implements Provider<CacheManager> {

	private static final int DEFAULT_MAX_CACHE_ENTRIES = 4000;
	private CacheManager cacheManager;

	public DefaultCacheManagerProvider()
	{
		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		config.setUpdateCheck(false);
		CacheConfiguration tokenCacheConfiguration = new CacheConfiguration().
				maxEntriesLocalHeap(DEFAULT_MAX_CACHE_ENTRIES).
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
