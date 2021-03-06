package com.riccardomalavolti.arcano.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import com.riccardomalavolti.arcano.repositories.UserRepository;

import io.restassured.RestAssured;

public class UserEndpointServiceIT extends JerseyTest {

private static final String USERS = UserEndpoint.ENDPOINT_PATH;
	
	private static final UUID playerId = UUID.randomUUID();
	private static final String playerUsername = "Mike";
	
	UserService userService;

	@Mock UserRepository userRepo;
	@Mock AuthorizationService authService;
	
	@SuppressWarnings("deprecation")
	@Override
    protected Application configure() {
        MockitoAnnotations.initMocks(this);
        
        userService = new UserService(userRepo, authService);
        forceSet(TestProperties.CONTAINER_PORT, "0");
		return new ResourceConfig(UserEndpoint.class).register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(userService).to(UserService.class);
			}
		});
    }
	
	@Before
	public void configureRestAssured() {
		RestAssured.baseURI = getBaseUri().toString();
    }
	
	@Test
	public void getPlayersList() {
		UUID id2 = UUID.randomUUID();
		String name2 = "Joe";
		User p1 = new User();
		p1.setId(playerId);
		p1.setUsername(playerUsername);
		
		User p2 = new User();
		p2.setId(id2);
		p2.setUsername(name2);
		List<User> players = new ArrayList<User>(Arrays.asList(p1, p2));
		
		when(userRepo.getAllUsers())
		.thenReturn(players);

        given().
            accept(MediaType.APPLICATION_JSON).
        when().
            get(USERS).
        then().
            statusCode(200).
            assertThat().
            body(
                    "[0].id", equalTo(playerId.toString()),
                    "[0].username", equalTo(playerUsername),
                    "[1].id", equalTo(id2.toString()),
                    "[1].username", equalTo(name2)
            );
	}
	
	@Test
	public void getPlayerByID() {
		User p = new User(playerId);
		p.setUsername(playerUsername);
		
		when(userRepo.getUserById(playerId))
			.thenReturn(Optional.of(p));

		given().
			accept(MediaType.APPLICATION_JSON).
		when().
			get(USERS + "/" + p.getId().toString()).
		then().
			statusCode(200).
			assertThat().
				body(
					"id", equalTo(playerId.toString()), 
					"username", equalTo(playerUsername)
					);
		}
	
	@Test
	public void testPostNewPlayer() {
		UUID createdId = UUID.randomUUID();
		
		User playerSent = new User();
		playerSent.setUsername(playerUsername);
		
		User playerRetuned = new User();
		playerRetuned.setId(createdId);
		playerRetuned.setUsername(playerUsername);

		JsonObject jsonSent = Json.createObjectBuilder()
									.add("username", playerUsername)
									.add("password", "verysecret")
									.build();

		when(userRepo.addNewUser(playerSent)).thenReturn(playerRetuned);

		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(jsonSent.toString()).
		when()
			.post(USERS).
		then()
			.statusCode(201)
			.assertThat().
				body(
						"id", equalTo(createdId.toString()), 
						"username", equalTo(playerUsername)
					)
				.header("Location", response -> endsWith(USERS + "/" + createdId));
	}
	
}
