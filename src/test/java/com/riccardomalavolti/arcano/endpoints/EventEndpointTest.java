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
		eOne.setId((long)(1));
		eOne.setJudgeList(new HashSet<User>());
		eOne.setPlayerList(new HashSet<User>());
		eOne.setAdminList(new HashSet<User>());
		Event eTwo = new Event();
		eTwo.setId((long)(2));
		eTwo.setJudgeList(new HashSet<User>());
		eTwo.setPlayerList(new HashSet<User>());
		eTwo.setAdminList(new HashSet<User>());
		
		List<Event> eventList = new ArrayList<>(Arrays.asList(eOne, eTwo));
		
		when(eventService.getAllEvents()).thenReturn(eventList);
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(EventEndpoint.BASE_PATH).
		then()
			.statusCode(200)
			.assertThat()
				.body(
						"[0].id", equalTo(eOne.getId().intValue()),
						"[1].id", equalTo(eTwo.getId().intValue())
					);
				
	}
	
	@Test
	public void testGetEventById() {
		// Should also return a list of URL of the associated matches.
		Long id = (long) 1;
		Event eOne = new Event();
		eOne.setId(id);
		eOne.setJudgeList(new HashSet<User>());
		eOne.setPlayerList(new HashSet<User>());
		eOne.setAdminList(new HashSet<User>());
		
		when(eventService.getEventById(id)).thenReturn(eOne);
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(EventEndpoint.BASE_PATH + "/" + id.toString()).
		then()
			.statusCode(200)
			.assertThat()
				.body(
						"id", equalTo(id.intValue())
						);
	}

	@Test
	public void testGetEventByIdShouldReturn404IfEventCantBeFound() {
		Long id = (long) 1;
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
		Event event = new Event();
		event.setName("Foo");
		
		Event createdEvent = new Event();
		createdEvent.setId((long)(1));
		createdEvent.setName("Foo");
		createdEvent.setJudgeList(new HashSet<User>());
		createdEvent.setPlayerList(new HashSet<User>());
		createdEvent.setAdminList(new HashSet<User>());
		
		when(eventService.createEvent(any(Event.class))).thenReturn(createdEvent);
		
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
						"id", equalTo(1),
						"name", equalTo("Foo")
					 )
				.header("Location", 
						response -> endsWith(EventEndpoint.BASE_PATH + "/"+ createdEvent.getId()));
	}
	
	
	@Test
	public void testEnrollAPlayerInAEvent() {
		Long eventId = (long) 1;
		Long playerId = (long) 2;
		String playerUsername = "Mike";
		
		User player = new User();
		player.setId(playerId);
		player.setUsername(playerUsername);
		
		when(eventService.enrollPlayerInEvent(player, eventId)).thenReturn(player);
		JsonObject playerJson = Json.createObjectBuilder()
					 .add("id", player.getId())
					 .add("username", player.getUsername())
				 .build();
		
		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(playerJson.toString()).
		when()
			// events/{event_id}/enroll
			.put(EventEndpoint.BASE_PATH + String.format("/%s/players", eventId)).
		then()
			.statusCode(202)
			.assertThat()
				.body(
						"id", equalTo(playerId.intValue())
					 );
	}
	
	@Test
	public void testEnrollInNonExistentEventShouldReturn404() {
		Long eventId = (long) 1;
		Long playerId = (long) 2;
		String playerUsername = "Mike";
		
		User player = new User();
		player.setId(playerId);
		player.setUsername(playerUsername);
		
		when(eventService.enrollPlayerInEvent(player, eventId))
			.thenAnswer(invocation -> {throw new NotFoundException("");});
		
		JsonObject playerJson = Json.createObjectBuilder()
					 .add("id", player.getId())
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
	
	

}
