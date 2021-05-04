package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.NotFoundException;
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

import com.riccardomalavolti.arcano.dto.EventBrief;
import com.riccardomalavolti.arcano.dto.EventMapper;
import com.riccardomalavolti.arcano.dto.UserBrief;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.endpoints.rest.EventEndpoint;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.EventService;

import io.restassured.RestAssured;


public class EventEndpointTest extends JerseyTest {
	
	@Mock private EventService eventService;
	
	@SuppressWarnings("deprecation")
	@Override
	protected Application configure(){
        MockitoAnnotations.initMocks(this);
        forceSet(TestProperties.CONTAINER_PORT, "0");
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
		
		List<EventBrief> eventList = new ArrayList<>(Arrays.asList(
				EventMapper.toEventBrief(eOne), 
				EventMapper.toEventBrief(eTwo)));
		
		when(eventService.getAllEvents()).thenReturn(eventList);
		
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
		
		when(eventService.getEventById(id)).thenReturn(EventMapper.toEventDetails(eOne));
		
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
	public void testGetEventByIdShouldReturn404IfEventCantBeFound() {
		UUID id = UUID.randomUUID();
		when(eventService.getEventById(id)).thenAnswer(invocation -> {throw new NotFoundException("");});
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(EventEndpoint.BASE_PATH + "/" + id.toString()).
		then()
			.statusCode(404);
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
		
		when(eventService.createEvent(any(Event.class))).thenReturn(EventMapper.toEventDetails(createdEvent));
		
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
		
		when(eventService.enrollPlayerInEvent(playerId, eventId)).thenReturn(EventMapper.toEventDetails(event));
		
		given()
			.contentType(MediaType.APPLICATION_JSON).
		when()
			// events/{event_id}/enroll
			.put(EventEndpoint.BASE_PATH + String.format("/%s/players/%s", eventId, playerId)).
		then()
			.statusCode(202)
			.assertThat()
				.body(
						"id", equalTo(eventId.toString())
					 );
	}
	
	@Test
	public void testEnrollInNonExistentEventShouldReturn404() {
		UUID eventId = UUID.randomUUID();
		UUID playerId = UUID.randomUUID();
		String playerUsername = "Mike";
		
		User player = new User();
		player.setId(playerId);
		player.setUsername(playerUsername);
		
		when(eventService.enrollPlayerInEvent(playerId, eventId))
			.thenAnswer(invocation -> {throw new NotFoundException("");});
		
		JsonObject playerJson = Json.createObjectBuilder()
					 .add("id", player.getId().toString())
					 .add("username", player.getUsername())
				 .build();
		
		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(playerJson.toString()).
		when()
			// events/{event_id}/enroll
			.post(EventEndpoint.BASE_PATH + String.format("/%s/enroll", eventId)).
		then()
			.statusCode(404);
	}
	
	@Test
	public void testGetPlayerListByEventId() {
		UUID eventId = UUID.randomUUID();
		Event event = new Event(eventId);
		
		User playerOne = new User(UUID.randomUUID());
		User playerTwo = new User(UUID.randomUUID());
		
		event.enrollPlayer(playerOne);
		event.enrollPlayer(playerTwo);
		
		List<UserBrief> playerList = new ArrayList<UserBrief>(Arrays.asList(
						UserMapper.toUserBrief(playerOne), 
						UserMapper.toUserBrief(playerTwo)));
		
		when(eventService.getPlayersForEvent(eventId)).thenReturn(playerList);
		
		
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
	
	@Test
	public void testGetJudgeListByEventId() {
		UUID eventId = UUID.randomUUID();
		Event event = new Event(eventId);
		
		User judgeOne = new User(UUID.randomUUID());
		User judgeTwo = new User(UUID.randomUUID());

		
		event.addJudge(judgeOne);
		event.addJudge(judgeTwo);
		
		List<UserBrief> playerList = new ArrayList<UserBrief>(Arrays.asList(
				UserMapper.toUserBrief(judgeOne), 
				UserMapper.toUserBrief(judgeTwo)));
		
		when(eventService.getJudgeList(eventId)).thenReturn(playerList);
		
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(EventEndpoint.BASE_PATH + String.format("/%s/judges", eventId)).
		then()
			.statusCode(200)
			.assertThat()
				.body(
						"[0].id", equalTo(judgeOne.getId().toString()),
						"[1].id", equalTo(judgeTwo.getId().toString())
					);
	}
	
}
