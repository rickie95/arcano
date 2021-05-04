package com.riccardomalavolti.arcano.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
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

import com.riccardomalavolti.arcano.endpoints.rest.EventEndpoint;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.EventRepository;

import io.restassured.RestAssured;

public class EventEndpointServiceIT extends JerseyTest {
	
	EventService eventService;
	
	@Mock EventRepository eventRepo;
	@Mock UserService userService;
	@Mock AuthorizationService authService;
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected Application configure(){
        MockitoAnnotations.initMocks(this);
        forceSet(TestProperties.CONTAINER_PORT, "0");

        eventService = new EventService(eventRepo, userService, authService);
        return new ResourceConfig(EventEndpoint.class)
            .register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(eventService).to(EventService.class);
                }
            });
    }
	
	@Before
	public void configureRestAssured() {
		RestAssured.baseURI = getBaseUri().toString();
	}
	
	@Test
	public void testGetEventList() {
		Event eOne = new Event();
		eOne.setId(UUID.randomUUID());
		eOne.setJudgeList(new HashSet<User>());
		eOne.setPlayerList(new HashSet<User>());
		eOne.setAdminList(new HashSet<User>());
		Event eTwo = new Event();
		eTwo.setId(UUID.randomUUID());
		eTwo.setJudgeList(new HashSet<User>());
		eTwo.setPlayerList(new HashSet<User>());
		eTwo.setAdminList(new HashSet<User>());
		
		List<Event> eventList = new ArrayList<>(Arrays.asList(eOne, eTwo));
		
		when(eventRepo.getAllEvents()).thenReturn(eventList);
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(EventEndpoint.BASE_PATH).
		then()
			.statusCode(200)
			.assertThat()
				.body(
						"[0].id", equalTo(eOne.getId().toString()),
						"[1].id", equalTo(eTwo.getId().toString())
					);
	}
	

	@Test
	public void testGetEventById() {
		// Should also return a list of URL of the associated matches.
		UUID id = UUID.randomUUID();
		Event eOne = new Event();
		eOne.setId(id);
		eOne.setJudgeList(new HashSet<User>());
		eOne.setPlayerList(new HashSet<User>());
		eOne.setAdminList(new HashSet<User>());
		
		when(eventRepo.getEventById(id)).thenReturn(Optional.of(eOne));
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(EventEndpoint.BASE_PATH + "/" + id.toString()).
		then()
			.statusCode(200)
			.assertThat()
				.body(
						"id", equalTo(id.toString())
						);
	}
	
	@Test
	public void testCreateNewEvent() {
		UUID eventId = UUID.randomUUID();
		Event event = new Event();
		event.setName("Foo");
		
		Event createdEvent = new Event(eventId);
		createdEvent.setName("Foo");
		createdEvent.setJudgeList(new HashSet<User>());
		createdEvent.setPlayerList(new HashSet<User>());
		createdEvent.setAdminList(new HashSet<User>());
		
		when(eventRepo.addEvent(any(Event.class))).thenReturn(createdEvent);
		
		JsonArray emptyArray = Json.createArrayBuilder().build();
		
		JsonObject eventJson = Json.createObjectBuilder()
					 .add("name", event.getName())
					 .add("playerList", emptyArray)
					 .add("adminList", emptyArray)
					 .add("judgeList", emptyArray)
				 .build();
		
		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(eventJson.toString()).
		when()
			.post(EventEndpoint.BASE_PATH).
		then()
			.statusCode(201)
			.assertThat()
				.body(
						"id", equalTo(eventId.toString()),
						"name", equalTo("Foo")
					 )
				.header("Location", 
						response -> endsWith(EventEndpoint.BASE_PATH + "/"+ createdEvent.getId()));
	}
	
	@Test
	public void testEnrollAPlayerInAEvent() {
		UUID eventId = UUID.randomUUID();
		Event event = new Event();
		event.setId(eventId);
		
		UUID playerId = UUID.randomUUID();
		String playerUsername = "Mike";
		
		User player = new User();
		player.setId(playerId);
		player.setUsername(playerUsername);
		
		when(userService.getUserById(playerId)).thenReturn(player);
		when(eventRepo.getEventById(eventId)).thenReturn(Optional.of(event));
		
		given()
			.contentType(MediaType.APPLICATION_JSON).
		when()
			// events/{event_id}/enroll
			.put(EventEndpoint.BASE_PATH + String.format("/%s/players/%s", eventId, playerId)).
		then()
			.statusCode(202)
			.assertThat()
				.body(
						"id", equalTo(eventId.toString()),
						"playerList[0].id", equalTo(player.getId().toString())
					 );
	}
	
	@Test
	public void testGetPlayerListByEventId() {
		UUID eventId = UUID.randomUUID();
		Event event = new Event(eventId);
		
		User playerOne = new User(UUID.randomUUID());
		User playerTwo = new User(UUID.randomUUID());
		
		event.enrollPlayer(playerOne);
		event.enrollPlayer(playerTwo);
		
		when(eventRepo.getEventById(eventId)).thenReturn(Optional.of(event));
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(EventEndpoint.BASE_PATH + String.format("/%s/players", eventId)).
		then()
			.statusCode(200)
			.assertThat()
				.body(
						"[0].id", equalTo(playerOne.getId().toString()),
						"[1].id", equalTo(playerTwo.getId().toString())
					);
	}

}
