package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.riccardomalavolti.arcano.endpoints.rest.UserEndpoint;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.UserService;

import io.restassured.RestAssured;

public class UserEndpointTest extends JerseyTest {
	
	private static final String USERS = UserEndpoint.ENDPOINT_PATH;
	
	private static final Long playerId = (long) 1;
	private static final String playerUsername = "Mike";

	@Mock
	private UserService playerService;
	
	@SuppressWarnings("deprecation")
	@Override
    protected Application configure() {
        MockitoAnnotations.initMocks(this);
        forceSet(TestProperties.CONTAINER_PORT, "0");
		return new ResourceConfig(UserEndpoint.class)
				.register(new AbstractBinder() {
				@Override
				protected void configure() {
					bind(playerService)
						.to(UserService.class);
				}
			});
    }
	
	@Before
	public void configureRestAssured() {
		RestAssured.baseURI = getBaseUri().toString();
    }

	@Test
	public void getPlayersList() {
		Long id1 = (long) 1;
		String name1 = "Mike";
		Long id2 = (long) 2;
		String name2 = "Joe";
		User p1 = new User();
		p1.setId(id1);
		p1.setUsername(name1);
		
		User p2 = new User();
		p2.setId(id2);
		p2.setUsername(name2);
		List<User> players = new ArrayList<User>(Arrays.asList(p1, p2));
		
		when(playerService.getAllUsers())
		.thenReturn(players);

        given().
            accept(MediaType.APPLICATION_JSON).
        when().
            get(USERS).
        then().
            statusCode(200).
            assertThat().
            body(
                    "[0].id", equalTo(id1.intValue()),
                    "[0].username", equalTo(name1),
                    "[1].id", equalTo(id2.intValue()),
                    "[1].username", equalTo(name2)
            );
	}

	@Test
	public void getPlayerByID() {
		Long id = (long) 1;
		String name = "Mike";
		User p = new User();
		p.setId(id);
		p.setUsername(name);
		
		when(playerService.getUserById(anyString()))
			.thenReturn(p);

		given().
			accept(MediaType.APPLICATION_JSON).
		when().
			get(USERS + "/1").
		then().
			statusCode(200).
			assertThat().
				body(
					"id", equalTo(id.intValue()), 
					"username", equalTo(name)
					);
		}

	@Test
	public void testPostNewPlayer() {
		Long createdId = (long) 3;
		String name = "Mike";
		
		User playerSent = new User();
		playerSent.setUsername(name);
		
		User playerRetuned = new User();
		playerRetuned.setId(createdId);
		playerRetuned.setUsername(name);

		JsonObject jsonSent = Json.createObjectBuilder()
									.add("username", name)
									.build();

		when(playerService.addNewUser(playerSent)).thenReturn(playerRetuned);

		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(jsonSent.toString()).
		when()
			.post(USERS).
		then()
			.statusCode(201)
			.assertThat().
				body(
						"id", equalTo(createdId.intValue()), 
						"username", equalTo(name)
					)
				.header("Location", response -> endsWith(USERS + "/" + createdId));
	}
	
	@Test
	public void testDeletePlayer() {
		User playerToBeDeleted = new User();
		playerToBeDeleted.setId(playerId);
		playerToBeDeleted.setUsername(playerUsername);
		
		when(playerService.deleteUser(playerId.toString())).thenReturn(playerToBeDeleted);
		
		when().
			delete(USERS + "/" + playerId).
		then().
			statusCode(202).
			assertThat().
				body(
						"id", equalTo(playerId.intValue()),
						"username", equalTo(playerUsername)
					);
			
	}
	
	@Test
	public void testPutPlayer() {
		String updatedName = "Joe";
		
		User playerSent = new User();
		playerSent.setId(playerId);
		playerSent.setUsername(playerUsername);
		
		User playerUpdated = new User();
		playerUpdated.setId(playerId);
		playerUpdated.setUsername(updatedName);
		
		JsonObject jsonSent = Json.createObjectBuilder()
				.add("id", playerId)
				.add("username", playerUsername)
				.build();
		
		when(playerService.updateUser(playerId.toString(), playerSent))
			.thenReturn(playerUpdated);
		
		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(jsonSent.toString()).
		when().
			put(USERS + "/" + playerId.toString()).
		then().
			statusCode(200).
			assertThat().
				body(
					"id", equalTo(playerId.intValue()),
					"username",  equalTo(updatedName)
						);
	}
	
}
