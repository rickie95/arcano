package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.endpoints.rest.UserEndpoint;
import com.riccardomalavolti.arcano.exceptions.ConflictException;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.UserService;
import com.sun.security.auth.UserPrincipal;

import io.restassured.RestAssured;

public class UserEndpointTest extends JerseyTest {
	
	private static final String USERS = UserEndpoint.ENDPOINT_PATH;
	
	private static final UUID playerId = UUID.randomUUID();
	private static final String playerUsername = "Mike";

	@Mock
	private UserService playerService;
	@Mock
	private SecurityContext securityContextMock;
	
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
			}).register(new ContainerRequestFilter() {
				@Override
				public void filter(final ContainerRequestContext containerRequestContext) throws IOException {
					containerRequestContext.setSecurityContext(securityContextMock);
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
		
		when(playerService.getAllUsers())
		.thenReturn(UserMapper.toUserBriefList(players));

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
                    "[0].uri", equalTo(getBaseUri().toString() + UserEndpoint.ENDPOINT_PATH + "/" + playerId.toString()),
                    "[1].id", equalTo(id2.toString()),
                    "[1].username", equalTo(name2),
                    "[1].uri", equalTo(getBaseUri().toString() + UserEndpoint.ENDPOINT_PATH + "/" + id2.toString())
            );
	}

	@Test
	public void getPlayerByID() {
		User p = new User(playerId);
		p.setUsername(playerUsername);
		
		when(playerService.getUserDetailsById(playerId))
			.thenReturn(UserMapper.toUserDetails(p));

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
	public void getPlayerByIDShouldReturn404IfUserDoesntExists() {
		User p = new User(playerId);
		p.setUsername(playerUsername);
		
		when(playerService.getUserDetailsById(playerId))
			.thenThrow(new NotFoundException());

		given().
			accept(MediaType.APPLICATION_JSON).
		when().
			get(USERS + "/" + p.getId().toString()).
		then().
			statusCode(404);
		}
	
	@Test
	public void getUserByUsername() {
		User user = new User(playerId);
		user.setUsername(playerUsername);
		
		when(playerService.getUserDetailsByUsername(user.getUsername()))
			.thenReturn(UserMapper.toUserDetails(user));
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(USERS + "/byUsername/"+ user.getUsername()).
		then().
			statusCode(200).
			assertThat().
				body(
						"id", equalTo(user.getId().toString()), 
						"username", equalTo(user.getUsername())
						);
		
	}
	
	@Test
	public void getUserByUsernameShouldReturn404IfPlayerDoesntExist() {
		User user = new User(playerId);
		user.setUsername(playerUsername);
		
		when(playerService.getUserDetailsByUsername(user.getUsername()))
			.thenThrow(new NotFoundException());
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(USERS + "/byUsername/"+ user.getUsername()).
		then().
			statusCode(404);
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
									.build();

		when(playerService.addNewUser(playerSent)).thenReturn(UserMapper.toUserDetails(playerRetuned));

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
	
	@Test
	public void testPostNewPlayerShouldReturn406IfAnotherPlayerHaveTheSameUsername() {
		UUID createdId = UUID.randomUUID();
		
		User playerSent = new User();
		playerSent.setUsername(playerUsername);
		
		User playerRetuned = new User();
		playerRetuned.setId(createdId);
		playerRetuned.setUsername(playerUsername);

		JsonObject jsonSent = Json.createObjectBuilder()
									.add("username", playerUsername)
									.build();

		when(playerService.addNewUser(playerSent)).thenThrow(new ConflictException());

		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(jsonSent.toString()).
		when()
			.post(USERS).
		then()
			.statusCode(409);
	}
	
	@Test
	public void testUpdateUserShouldReturnUpdatedUser() {
		User toBeUpdated = new User(UUID.randomUUID());
		toBeUpdated.setUsername(playerUsername);
		toBeUpdated.setName("MIKE");
				
		User updatedUser = new User(toBeUpdated.getId());
		updatedUser.setUsername(playerUsername);
		updatedUser.setName("Mike");
		
		JsonObject jsonSent = Json.createObjectBuilder()
				.add("id", updatedUser.getId().toString())
				.add("username", updatedUser.getUsername())
				.add("name", updatedUser.getName())
				.build();
		
		when(playerService.updateUser(toBeUpdated.getId(), updatedUser, toBeUpdated.getUsername()))
		.thenReturn(UserMapper.toUserDetails(updatedUser));
		
		UserPrincipal userPrincipal = new UserPrincipal(toBeUpdated.getUsername());
		when(securityContextMock.getUserPrincipal()).thenReturn(userPrincipal);
		
		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(jsonSent.toString()).
		when()
			.put(USERS + "/" + toBeUpdated.getId().toString()).
		then()
			.statusCode(200)
			.assertThat().
				body(
						"id", equalTo(updatedUser.getId().toString()),
						"username", equalTo(updatedUser.getUsername()),
						"name", equalTo(updatedUser.getName())
					);
	}
	
	@Test
	public void testUpdateUserShouldReturn404IfPlayerToBeUpdatedDoesntExists() {
		User toBeUpdated = new User(UUID.randomUUID());
		toBeUpdated.setUsername(playerUsername);
		toBeUpdated.setName("MIKE");
		
		User updatedUser = new User(toBeUpdated.getId());
		updatedUser.setUsername(playerUsername);
		updatedUser.setName("Mike");
		
		JsonObject jsonSent = Json.createObjectBuilder()
				.add("id", updatedUser.getId().toString())
				.add("username", updatedUser.getUsername())
				.add("name", updatedUser.getName())
				.build();
		
		when(playerService.updateUser(toBeUpdated.getId(), updatedUser, toBeUpdated.getUsername()))
		.thenThrow(new NotFoundException());
		
		UserPrincipal userPrincipal = new UserPrincipal(toBeUpdated.getUsername());
		when(securityContextMock.getUserPrincipal()).thenReturn(userPrincipal);
		
		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(jsonSent.toString()).
		when()
			.put(USERS + "/" + toBeUpdated.getId().toString()).
		then()
			.statusCode(404);
	}
	
	@Test
	public void testUpdateUserShouldReturn403IfUserIsTryingToModifyAnotherUser() {
		String notAuthorizedUsername = "NotAuthorizedUsername";
		User toBeUpdated = new User(UUID.randomUUID());
		toBeUpdated.setUsername(playerUsername);
		toBeUpdated.setName("MIKE");
		
		User updatedUser = new User(toBeUpdated.getId());
		updatedUser.setUsername(playerUsername);
		updatedUser.setName("Mike");
		
		JsonObject jsonSent = Json.createObjectBuilder()
				.add("id", updatedUser.getId().toString())
				.add("username", updatedUser.getUsername())
				.add("name", updatedUser.getName())
				.build();
		
		when(playerService.updateUser(toBeUpdated.getId(), updatedUser, notAuthorizedUsername))
		.thenThrow(new ForbiddenException());
		
		UserPrincipal userPrincipal = new UserPrincipal(notAuthorizedUsername);
		when(securityContextMock.getUserPrincipal()).thenReturn(userPrincipal);
		
		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(jsonSent.toString()).
		when()
			.put(USERS + "/" + toBeUpdated.getId().toString()).
		then()
			.statusCode(403);
	}
	
	@Test
	public void deleteUserShouldReturn201AndTheDeletedUser() {
		User toBeDeleted = new User(UUID.randomUUID());
		toBeDeleted.setUsername(playerUsername);
		toBeDeleted.setName("Mike");
		
		UserPrincipal authorizedUserPrincipal = new UserPrincipal(toBeDeleted.getUsername());
		when(securityContextMock.getUserPrincipal()).thenReturn(authorizedUserPrincipal);
		
		when(playerService.deleteUser(toBeDeleted.getId(), toBeDeleted.getUsername()))
			.thenReturn(UserMapper.toUserDetails(toBeDeleted));
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.delete(USERS + "/" + toBeDeleted.getId().toString()).
		then()
			.statusCode(202)
			.assertThat().body(
					"id", equalTo(toBeDeleted.getId().toString()),
					"username", equalTo(toBeDeleted.getUsername())
					);
	}
	
	@Test
	public void deleteUserShouldReturn404IfUserDoesntExist() {
		User toBeDeleted = new User(UUID.randomUUID());
		toBeDeleted.setUsername(playerUsername);
		toBeDeleted.setName("Mike");
		
		UserPrincipal authorizedUserPrincipal = new UserPrincipal(toBeDeleted.getUsername());
		when(securityContextMock.getUserPrincipal()).thenReturn(authorizedUserPrincipal);
		
		when(playerService.deleteUser(toBeDeleted.getId(), toBeDeleted.getUsername()))
			.thenThrow(new NotFoundException());
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.delete(USERS + "/" + toBeDeleted.getId().toString()).
		then()
			.statusCode(404);
	}
	
	@Test
	public void deleteUserShouldReturn403IfAnotherUserTriesToDelete() {
		User toBeDeleted = new User(UUID.randomUUID());
		toBeDeleted.setUsername(playerUsername);
		toBeDeleted.setName("Mike");
		
		String notAuthorizedUsername = "NotMike";
		UserPrincipal authorizedUserPrincipal = new UserPrincipal(notAuthorizedUsername);
		when(securityContextMock.getUserPrincipal()).thenReturn(authorizedUserPrincipal);
		
		when(playerService.deleteUser(toBeDeleted.getId(), notAuthorizedUsername))
			.thenThrow(new ForbiddenException());
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.delete(USERS + "/" + toBeDeleted.getId().toString()).
		then()
			.statusCode(403);
	}
	
}
