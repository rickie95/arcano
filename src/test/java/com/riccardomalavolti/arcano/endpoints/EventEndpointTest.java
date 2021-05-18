package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
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

import com.riccardomalavolti.arcano.dto.EventBrief;
import com.riccardomalavolti.arcano.dto.EventDetails;
import com.riccardomalavolti.arcano.dto.EventMapper;
import com.riccardomalavolti.arcano.dto.UserBrief;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.endpoints.rest.EventEndpoint;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.EventStatus;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.EventService;
import com.sun.security.auth.UserPrincipal;

import io.restassured.RestAssured;


public class EventEndpointTest extends JerseyTest {
	
	private Event event;
	private User admin;
	private LocalDateTime startingTime;
	
	@Mock private EventService eventService;
	@Mock private SecurityContext securityContextMock;
	
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
	
	@Before
	public void initFields() {
		admin = new User(UUID.randomUUID());
		admin.setName("Admin");
		admin.setPassword("secret");
		admin.setUsername("admin420");
		
		startingTime = LocalDateTime.of(2021, 5, 7, 12, 45);
		
		event = new Event(UUID.randomUUID());
		event.setName("Foo");
		event.setJudgeList(new HashSet<User>());
		event.setPlayerList(new HashSet<User>());
		event.setAdminList( Set.of(admin));
		event.setStartingTime(startingTime);
		event.setStatus(EventStatus.SCHEDULED);
	}
	
	@Test
	public void testGetEventList() {
		EventBrief eOne = new EventBrief();
		eOne.setId(UUID.randomUUID());
		eOne.setName("FOO");
		eOne.setUri(getBaseUri().toString() + EventEndpoint.BASE_PATH + "/" + eOne.getId().toString());
		
		EventBrief eTwo = new EventBrief();
		eTwo.setId(UUID.randomUUID());
		eTwo.setName("BAR");
		eTwo.setUri(getBaseUri().toString() + EventEndpoint.BASE_PATH + "/" + eTwo.getId().toString());
		
		List<EventBrief> eventList = new ArrayList<>(Arrays.asList(
				eOne, eTwo));
		
		when(eventService.getAllEvents()).thenReturn(eventList);
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(EventEndpoint.BASE_PATH).
		then()
			.statusCode(200)
			.log().body()
			.assertThat()
				.body(
						"[0].id", equalTo(eOne.getId().toString()),
						"[0].name", equalTo(eOne.getName()),
						"[0].uri", equalTo(eOne.getUri().toString()),
						"[1].id", equalTo(eTwo.getId().toString()),
						"[1].name", equalTo(eTwo.getName()),
						"[1].uri", equalTo(eTwo.getUri().toString())
					);
				
	}
	
	@Test
	public void testGetEventById() {
		// Should also return a list of URL of the associated matches.
		UUID id = UUID.randomUUID();
		EventDetails event = new EventDetails();
		event.setId(id);
		event.setJudgeList(new HashSet<UserBrief>());
		event.setPlayerList(new HashSet<UserBrief>());
		event.setAdminList(new HashSet<UserBrief>());
		event.setStartingTime(LocalDateTime.of(2021, 5, 7, 12, 45));
		event.setStatus(EventStatus.IN_PROGRESS);
		
		when(eventService.getEventById(id)).thenReturn(event);
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(EventEndpoint.BASE_PATH + "/" + id.toString()).
		then()
			.statusCode(200)
			.assertThat()
				.body(
						"id", equalTo(id.toString()),
						"startingTime", equalTo(event.getStartingTime().format(EventDetails.DATE_TIME_FORMATTER)),
						"status", equalTo(event.getStatus().toString())
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
		event.setStartingTime(startingTime);
		event.setStatus(EventStatus.IN_PROGRESS);
		event.setRound(0);
		event.setType("Limited");
		
		Event createdEvent = new Event(eventId);
		createdEvent.setName("Foo");
		createdEvent.setJudgeList(new HashSet<User>());
		createdEvent.setPlayerList(new HashSet<User>());
		createdEvent.setAdminList( Set.of(admin));
		createdEvent.setStartingTime(startingTime);
		createdEvent.setStatus(EventStatus.IN_PROGRESS);
		createdEvent.setRound(event.getRound());
		createdEvent.setType(event.getType());
		
		when(eventService.createEvent(any(Event.class))).thenReturn(EventMapper.toEventDetails(createdEvent));
		
		JsonArray emptyArray = Json.createArrayBuilder().build();
		
		JsonObject userJson = Json.createObjectBuilder()
				.add("id", admin.getId().toString())
				.add("username", admin.getUsername())
				.build();
		
		JsonObject eventJson = Json.createObjectBuilder()
					 .add("name", event.getName())
					 .add("playerList", emptyArray)
					 .add("adminList", Json.createArrayBuilder().add(userJson).build())
					 .add("judgeList", emptyArray)
					 .add("startingTime", event.getStartingTime().format(EventDetails.DATE_TIME_FORMATTER))
					 .add("eventStatus", EventStatus.IN_PROGRESS.toString())
					 .add("type", event.getType())
					 .add("round", event.getRound())
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
						"name", equalTo("Foo"),
						"playerList.size()", equalTo(0),
						"adminList.size()", equalTo(1),
						"judgeList.size()", equalTo(0),
						"startingTime", equalTo(event.getStartingTime().format(EventDetails.DATE_TIME_FORMATTER)),
						"status", equalTo(event.getStatus().toString())
					 )
				.header("Location", 
						response -> endsWith(EventEndpoint.BASE_PATH + "/"+ createdEvent.getId()));
	}
	
	@Test
	public void testCreateNewEventWithStatus() {
		User admin = new User(UUID.randomUUID());
		admin.setName("Admin");
		admin.setPassword("secret");
		admin.setUsername("admin420");
		
		LocalDateTime startingTime = LocalDateTime.of(2021, 5, 7, 12, 45);
		Event event = new Event(UUID.randomUUID());
		event.setName("Foo");
		event.setStartingTime(startingTime);
		event.setStatus(EventStatus.SCHEDULED);
		
		Event createdEvent = new Event(event.getId());
		createdEvent.setName("Foo");
		createdEvent.setJudgeList(new HashSet<User>());
		createdEvent.setPlayerList(new HashSet<User>());
		createdEvent.setAdminList( Set.of(admin));
		createdEvent.setStartingTime(startingTime);
		createdEvent.setStatus(EventStatus.SCHEDULED);
		
		when(eventService.createEvent(any(Event.class))).thenReturn(EventMapper.toEventDetails(createdEvent));
		
		JsonArray emptyArray = Json.createArrayBuilder().build();
		
		JsonObject userJson = Json.createObjectBuilder()
				.add("id", admin.getId().toString())
				.add("username", admin.getUsername())
				.build();
		
		JsonObject eventJson = Json.createObjectBuilder()
					 .add("name", event.getName())
					 .add("playerList", emptyArray)
					 .add("adminList", Json.createArrayBuilder().add(userJson).build())
					 .add("judgeList", emptyArray)
					 .add("startingTime", event.getStartingTime().format(EventDetails.DATE_TIME_FORMATTER))
					 .add("status", EventStatus.SCHEDULED.toString())
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
						"id", equalTo(event.getId().toString()),
						"name", equalTo("Foo"),
						"playerList.size()", equalTo(0),
						"adminList.size()", equalTo(1),
						"judgeList.size()", equalTo(0),
						"startingTime", equalTo(event.getStartingTime().format(EventDetails.DATE_TIME_FORMATTER)),
						"status", equalTo(event.getStatus().toString())
					 )
				.header("Location", 
						response -> endsWith(EventEndpoint.BASE_PATH + "/"+ createdEvent.getId()));
	}
	
	@Test
	public void updateEventShoudlReturn202() {		
		
		Event eventToBeUpdated = event;
		
		Event updatedEvent = new Event(event.getId());
		updatedEvent.setName("Foo");
		updatedEvent.setJudgeList(new HashSet<User>());
		updatedEvent.setPlayerList(new HashSet<User>());
		updatedEvent.setAdminList( Set.of(admin));
		updatedEvent.setStartingTime(startingTime);
		updatedEvent.setStatus(EventStatus.SCHEDULED);
		
		JsonArray emptyArray = Json.createArrayBuilder().build();
		
		JsonObject userJson = Json.createObjectBuilder()
				.add("id", admin.getId().toString())
				.add("username", admin.getUsername())
				.build();
		
		JsonObject updatedEventJsonBody = Json.createObjectBuilder()
				.add("id", updatedEvent.getId().toString())
				 .add("adminList", Json.createArrayBuilder().add(userJson).build())
				 .add("startingTime", updatedEvent.getStartingTime().format(EventDetails.DATE_TIME_FORMATTER))
				 .build();
		
		UserPrincipal userPrincipal = new UserPrincipal(admin.getUsername());
		when(securityContextMock.getUserPrincipal()).thenReturn(userPrincipal);
		
		when(eventService.updateEvent(eventToBeUpdated.getId(), eventToBeUpdated, admin.getUsername()))
			.thenReturn(EventMapper.toEventDetails(updatedEvent));
		
		
		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(updatedEventJsonBody.toString())
			.log().all()
		.when()
			.put(EventEndpoint.BASE_PATH +"/" + eventToBeUpdated.getId().toString())
		.then()
		.log().all()
			.statusCode(200)
			.assertThat()
			.body(
					"id", equalTo(event.getId().toString()),
					"startingTime", equalTo(eventToBeUpdated.getStartingTime().format(EventDetails.DATE_TIME_FORMATTER).toString())
					);
		
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
