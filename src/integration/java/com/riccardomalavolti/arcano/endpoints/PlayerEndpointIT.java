package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.restassured.RestAssured;

@ExtendWith(MockitoExtension.class)
class PlayerEndpointIT {

	private HttpServer server;	

	@BeforeClass
	public static void configure() {
		RestAssured.baseURI = BasicServer.BASE_URI;
		
	}

	@BeforeEach
	public void setUp() throws Exception {
		server = BasicServer.startServer();
	}

	@AfterEach
	public void tearDown() throws Exception {
		server.shutdownNow();
	}
	
	
	@Test
	void getPlayersList() {
		given().
			accept(MediaType.APPLICATION_JSON)
			.when().
			get(BasicServer.BASE_URI + "player").
		then().
			statusCode(200).
			assertThat().
				body(
					"id[0]", equalTo(1),
					"id[1]", equalTo(2)
				);

	}

	@Test
	void getPlayerByID() {
		final int playerId = 1;
		final String url = String.format(BasicServer.BASE_URI + "player/%s", playerId);

		
		given().
			accept(MediaType.APPLICATION_JSON).
		when().
			get(url).
		then().
			statusCode(200).
			assertThat().
				body(
						"id", equalTo(playerId),
						"name", equalTo("Mike")
					);
	}
}
