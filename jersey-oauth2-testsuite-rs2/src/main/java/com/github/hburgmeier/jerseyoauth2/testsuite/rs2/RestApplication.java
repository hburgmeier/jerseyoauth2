package com.github.hburgmeier.jerseyoauth2.testsuite.rs2;

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

import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientIdGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.DefaultPrincipalUserService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.IntegratedAccessTokenVerifier;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.MD5TokenGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.UUIDClientIdGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.jpa.CachingAccessTokenStorage;
import com.github.hburgmeier.jerseyoauth2.authsrv.jpa.DatabaseClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.jpa.guice.DefaultCacheManagerProvider;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.RequestFactory;
import com.github.hburgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.github.hburgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.github.hburgmeier.jerseyoauth2.rs.impl.rs2.filter.OAuth2FilterFeature;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.resource.ClientAuthResource;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.resource.ClientsResource;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.resource.TokenInvalidateResource;
import com.github.hburgmeier.jerseyoauth2.testsuite.rs2.resource.Sample2Resource;
import com.github.hburgmeier.jerseyoauth2.testsuite.rs2.resource.SampleResource;
import com.github.hburgmeier.jerseyoauth2.testsuite.rs2.services.Configuration;
import com.github.hburgmeier.jerseyoauth2.testsuite.rs2.services.PersistenceProvider;

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
        Injections.addBinding(Injections.newBinder(RequestFactory.class).to(IRequestFactory.class),dc);
        Injections.addBinding(Injections.newBinder(MD5TokenGenerator.class).to(ITokenGenerator.class),dc);
        Injections.addBinding(Injections.newBinder(UUIDClientIdGenerator.class).to(IClientIdGenerator.class),dc);
		
		EntityManagerFactory emf = new PersistenceProvider().get();
        Injections.addBinding(Injections.newBinder(emf).to(EntityManagerFactory.class),dc);
        
        CacheManager cacheManager = new DefaultCacheManagerProvider().get();
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
