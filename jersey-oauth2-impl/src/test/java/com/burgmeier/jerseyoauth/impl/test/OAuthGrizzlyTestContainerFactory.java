/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.burgmeier.jerseyoauth.impl.test;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.container.ContainerFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainer;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;

/**
 * A low-level test container factory for creating test container instances
 * using Grizzly 2.
 *
 * @author Paul.Sandoz@Sun.COM
 * @author pavel.bucek@oracle.com
 */
public class OAuthGrizzlyTestContainerFactory implements TestContainerFactory {

	private Injector injector;

	public OAuthGrizzlyTestContainerFactory(Module... modules)
	{
		injector = Guice.createInjector(modules);		
	}
	
    @SuppressWarnings("unchecked")
	public Class<LowLevelAppDescriptor> supports() {
        return LowLevelAppDescriptor.class;
    }

    public TestContainer create(URI baseUri, AppDescriptor ad) {
        if (!(ad instanceof LowLevelAppDescriptor))
            throw new IllegalArgumentException(
                    "The application descriptor must be an instance of LowLevelAppDescriptor");
        
        return new GrizzlyTestContainer(baseUri, (LowLevelAppDescriptor)ad, injector);
    }

    /**
     * This class has methods for instantiating, starting and stopping the light-weight
     * Grizzly 2 server.
     */
    private static class GrizzlyTestContainer implements TestContainer {
        private static final Logger LOGGER =
                Logger.getLogger(GrizzlyTestContainer.class.getName());
        
        final HttpServer httpServer;

        final URI baseUri;

        /**
         * Creates an instance of {@link GrizzlyTestContainer}
         * @param baseUri URI of the application
         * @param ad instance of {@link LowLevelAppDescriptor}
         */
        GrizzlyTestContainer(URI baseUri, LowLevelAppDescriptor ad, Injector injector) {
			this.baseUri = UriBuilder.fromUri(baseUri).path(ad.getContextPath()).build();
            
            LOGGER.info("Creating low level grizzly2 container configured at the base URI " + this.baseUri);
            try {
                HttpHandler httpHandler = ContainerFactory.createContainer(HttpHandler.class,
                        ad.getResourceConfig(), new GuiceComponentProviderFactory(ad.getResourceConfig(), injector));
                this.httpServer = create(this.baseUri, httpHandler);
            } catch (Exception ex) {
                throw new TestContainerException(ex);
            }
        }

        public Client getClient() {
            return null;
        }

        public URI getBaseUri() {
            return baseUri;
        }

        public void start() {
            try {
                LOGGER.info("Starting low level grizzly2 container");
                httpServer.start();
            } catch (IOException ex) {
                throw new TestContainerException(ex);
            }
        }

        public void stop() {
            if (httpServer.isStarted()) {
                LOGGER.info("Stopping low level grizzly2 container");
                httpServer.stop();
            }
        }

        /**
         * Creates an instance of {@link HttpServer}
         * @param u The application base URI
         * @param handler An instance of {@link HttpHandler}
         * @return A {@link HttpServer} instance
         * @throws IOException
         * @throws IllegalArgumentException
         */
        private static HttpServer create(URI u, HttpHandler handler)
                throws IOException, IllegalArgumentException {
            if (u == null)
                throw new IllegalArgumentException("The URI must not be null");

            // TODO support https
            final String scheme = u.getScheme();
            if (!scheme.equalsIgnoreCase("http"))
                throw new IllegalArgumentException("The URI scheme, of the URI " + u +
                        ", must be equal (ignoring case) to 'http'");

            final String host = (u.getHost() == null) ? NetworkListener.DEFAULT_NETWORK_HOST
                    : u.getHost();
            final int port = (u.getPort() == -1) ? 80 : u.getPort();

            final HttpServer server = new HttpServer();
            final NetworkListener listener = new NetworkListener("grizzly", host, port);
            server.addListener(listener);

            // Map the path to the processor.
            final ServerConfiguration config = server.getServerConfiguration();
            config.addHttpHandler(handler, u.getPath());

            // Start the server.
            return server;
        }
    }
}