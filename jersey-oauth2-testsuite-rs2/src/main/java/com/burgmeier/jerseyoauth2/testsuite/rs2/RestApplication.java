package com.burgmeier.jerseyoauth2.testsuite.rs2;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Application;

import net.sf.ehcache.CacheManager;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.burgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.burgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.burgmeier.jerseyoauth2.authsrv.impl.services.DefaultPrincipalUserService;
import com.burgmeier.jerseyoauth2.authsrv.impl.services.IntegratedAccessTokenVerifier;
import com.burgmeier.jerseyoauth2.authsrv.jpa.CachingAccessTokenStorage;
import com.burgmeier.jerseyoauth2.authsrv.jpa.DatabaseClientService;
import com.burgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.burgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.burgmeier.jerseyoauth2.rs.impl.rs2.filter.OAuth2FilterFeature;
import com.burgmeier.jerseyoauth2.testsuite.rs2.resource.ClientAuthResource;
import com.burgmeier.jerseyoauth2.testsuite.rs2.resource.ClientsResource;
import com.burgmeier.jerseyoauth2.testsuite.rs2.resource.Sample2Resource;
import com.burgmeier.jerseyoauth2.testsuite.rs2.resource.SampleResource;
import com.burgmeier.jerseyoauth2.testsuite.rs2.resource.TokenInvalidateResource;
import com.burgmeier.jerseyoauth2.testsuite.rs2.services.CacheManagerProvider;
import com.burgmeier.jerseyoauth2.testsuite.rs2.services.Configuration;
import com.burgmeier.jerseyoauth2.testsuite.rs2.services.PersistenceProvider;

public class RestApplication extends Application {

	@Inject
    public RestApplication(ServiceLocator serviceLocator) {
		DynamicConfiguration dc = Injections.getConfiguration(serviceLocator);

        Injections.addBinding(Injections.newBinder(DatabaseClientService.class).to(IClientService.class),dc);
        Injections.addBinding(Injections.newBinder(Configuration.class).to(IConfiguration.class),dc);
        Injections.addBinding(Injections.newBinder(Configuration.class).to(IRSConfiguration.class),dc);
        Injections.addBinding(Injections.newBinder(DefaultPrincipalUserService.class).to(IUserService.class),dc);
        Injections.addBinding(Injections.newBinder(CachingAccessTokenStorage.class).to(IAccessTokenStorageService.class),dc);
        Injections.addBinding(Injections.newBinder(IntegratedAccessTokenVerifier.class).to(IAccessTokenVerifier.class),dc);
		
		EntityManagerFactory emf = new PersistenceProvider().get();
        Injections.addBinding(Injections.newBinder(emf).to(EntityManagerFactory.class),dc);
        
        CacheManager cacheManager = new CacheManagerProvider().get();
        Injections.addBinding(Injections.newBinder(cacheManager).to(CacheManager.class),dc);
        
        dc.commit();
	}

	@Override
	public Set<Class<?>> getClasses() {
        Set<Class<?>> clazzes = new HashSet<Class<?>>();
        clazzes.add(ClientsResource.class);
        clazzes.add(ClientAuthResource.class);
        clazzes.add(SampleResource.class);
        clazzes.add(Sample2Resource.class);
        clazzes.add(TokenInvalidateResource.class);
        
        clazzes.add(JacksonFeature.class);
        
        clazzes.add(OAuth2FilterFeature.class);
        
        return clazzes;
	}
}
