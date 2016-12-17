package com.gsd.pos.agent.server;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

import org.apache.log4j.Logger;

import com.gsd.pos.utils.Config;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

public class MyServer {
	private static final int PORT = Config.getIntProperty("PORT", 8443);
	private static final Logger logger = Logger.getLogger(MyServer.class
			.getName());
//	@Inject
	private HttpHandler  reportHandler = new ReportHandler();
	private HttpHandler  versionHandler = new VersionHandler();

	@SuppressWarnings("restriction")
	public void start() throws Exception {
		InetSocketAddress addr = new InetSocketAddress(PORT);
		@SuppressWarnings("restriction")
		HttpsServer server = HttpsServer.create(addr, 0);
		try {
			logger.trace("Initializing context ...");
			KeyStore ks = KeyStore.getInstance("JKS");
			char[] password = "password".toCharArray();
			ks.load(new FileInputStream("posagent.ks"), password);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, password);
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), null, null);
			// a HTTPS server must have a configurator for the SSL connections.
			server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
				// override configure to change default configuration.
				public void configure(HttpsParameters params) {
					try {
						// get SSL context for this configurator
						SSLContext c = getSSLContext();
						// get the default settings for this SSL context
						SSLParameters sslparams = c.getDefaultSSLParameters();
						// set parameters for the HTTPS connection.
						params.setNeedClientAuth(true);
						params.setSSLParameters(sslparams);
						logger.trace("SSL context created ...\n");
					} catch (Exception e2) {
						System.out.println("Invalid parameter ...\n");
						e2.printStackTrace();
					}
				}
			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		HttpContext rptContext =  server.createContext("/reports", reportHandler);
		rptContext.getFilters().add(new ParameterFilter());
		HttpContext versionContext =  server.createContext("/version", versionHandler);
		server.setExecutor(Executors.newCachedThreadPool());
		logger.trace("Starting server, waiting for requests ...");
		server.start();
	}
}
