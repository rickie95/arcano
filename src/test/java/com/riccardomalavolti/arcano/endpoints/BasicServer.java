package com.riccardomalavolti.arcano.endpoints;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.riccardomalavolti.arcano.service.PlayerService;
import com.riccardomalavolti.arcano.service.PlayerServiceImp;

public class BasicServer {

	public static final String BASE_URI = "http://localhost:8080/arcano/";

	public static HttpServer startServer() {
		final ResourceConfig rc = new ResourceConfig()
				.packages("com.riccardomalavolti.arcano")
				.register(new AbstractBinder() {
					
					@Override
					protected void configure() {
						bind(PlayerServiceImp.class)
							.to(PlayerService.class);
						
					}
				});
		
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	public static void main(String[] args) throws IOException {
		final HttpServer server = startServer();
		System.out.println(String.format(
				"Jersey app started with WADL available at %sapplication.wadl\nHit enter to stop it...", BASE_URI));
		System.in.read();
		server.shutdownNow();
	}
}
