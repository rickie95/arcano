package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.dto.EventBrief;
import com.riccardomalavolti.arcano.dto.EventDetails;
import com.riccardomalavolti.arcano.dto.EventMapper;
import com.riccardomalavolti.arcano.dto.UserBrief;
import com.riccardomalavolti.arcano.dto.UserDetails;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.exceptions.AccessDeniedException;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.EventRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EventServiceTest {
	
	Event eventOne;
	Event eventTwo;
	
	@Mock EventRepository eventRepo;
	@Mock UserService userService;
	@Mock AuthorizationService authService;
	
	@InjectMocks EventService eventService;
	
	@BeforeEach
	void setupResources() {
		eventOne = new Event(UUID.randomUUID());
		eventTwo = new Event(UUID.randomUUID());
	}
	
	@Test
	void testGetAllEventShouldRetrunAListOfEvents() {
		List<Event> eventList = new ArrayList<Event>(Arrays.asList(eventOne, eventTwo));
		when(eventRepo.getAllEvents()).thenReturn(eventList);
		
		List<EventBrief> returnedList = eventService.getAllEvents();
		
		assertThat(returnedList).contains(
				EventMapper.toEventBrief(eventOne), 
				EventMapper.toEventBrief(eventTwo));
	}
	
	@Test
	void testGetAllEventsShouldReturnAnEmptyListIfTheresNoEventAvailable() {
		List<Event> eventList = new ArrayList<Event>();
		when(eventRepo.getAllEvents()).thenReturn(eventList);
		
		List<EventBrief> returnedList = eventService.getAllEvents();
		
		assertThat(returnedList).isEmpty();
	}
	
	@Test
	void testGetEventById() {		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		
		EventDetails result = eventService.getEventById(eventOne.getId());
		
		assertThat(result.getId()).isEqualTo(eventOne.getId());
	}
	
	@Test
	void testGetEventByIdShouldThrowNotFoundExceptionIfEventCantBeFound() {
		UUID eventUIID = UUID.randomUUID();
		
		when(eventRepo.getEventById(eventUIID)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> 
				eventService.getEventById(eventUIID)
			).isInstanceOf(NotFoundException.class);
	}
	
	@Test
	void testCreateEvent() {
		when(eventRepo.addEvent(eventOne)).thenReturn(eventOne);
		EventDetails eventCreated = eventService.createEvent(eventOne);
		assertThat(eventCreated.getId()).isEqualTo(eventOne.getId());
		}
	
	@Test
	void testRemoveAnEvent() {
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(eventRepo.removeEvent(eventOne)).thenReturn(eventOne);
		EventDetails removedEvent = eventService.removeEvent(eventOne.getId(), "ALLOWED");
		
		assertThat(removedEvent).isNotNull();
		assertThat(removedEvent.getId()).isEqualTo(eventOne.getId());
	}
	
	@Test
	void testUpdateEvent() {
		Event toBeUpdated = new Event();
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(eventRepo.updateEvent(toBeUpdated)).thenReturn(toBeUpdated);
		
		EventDetails updatedEvent = eventService.updateEvent(eventOne.getId(), toBeUpdated, "ALLOWED");
		
		assertThat(updatedEvent.getId()).isEqualTo(eventOne.getId());
	}
		
	@Test
	void testEnrollPlayerInAEvent() {
		User user = new User();
		user.setId(UUID.randomUUID());
		
		eventOne.enrollPlayer(user);
		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		
		EventDetails result = eventService.enrollPlayerInEvent(user.getId(), eventOne.getId());
		
		assertThat(result.getId()).isEqualTo(eventOne.getId());
		assertThat(result.getPlayerList()).contains(UserMapper.toUserBrief(user));
	}
	
	@Test
	void testEnrollPlayerInEventShouldThrowNotFoundExceptionIfUserCantBeFound() {
		UUID eventId = eventOne.getId();
		UUID userId = UUID.randomUUID();
		User user = new User();
		user.setId(userId);
		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(userService.getUserById(user.getId())).thenThrow(NotFoundException.class);
		
		assertThatThrownBy(
				() -> eventService.enrollPlayerInEvent(userId, eventId)
			).isInstanceOf(BadRequestException.class);
	}
	
	@Test
	void testEnrollPlayerInEventShouldThrowNotFoundExceptionIfEventCantBeFound() {
		UUID userId = UUID.randomUUID();
		User user = new User();
		user.setId(userId);
		
		UUID eventID = UUID.randomUUID();
		
		when(eventRepo.getEventById(eventID)).thenThrow(NotFoundException.class);
		
		assertThatThrownBy(
				() -> eventService.enrollPlayerInEvent(userId, eventID)
			).isInstanceOf(NotFoundException.class);
	}
	
	@Test
	void removePlayerFromEvent() {
		UUID userId = UUID.randomUUID();;
		User user = new User();
		user.setId(userId);
		
		eventOne.enrollPlayer(user);
		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(userService.getUserById(userId)).thenReturn(user);
		
		EventDetails result = eventService.removePlayerFromEvent(user.getId(), eventOne.getId(), "FOO");
		
		assertThat(result.getId()).isEqualTo(eventOne.getId());
		assertThat(result.getPlayerList()).doesNotContain(UserMapper.toUserBrief(user));
	}
	
	@Test
	void removePlayerFromEventSholdThrowNotFoundExceptionIfEventCantBeFound() {
		UUID eventOneId = eventOne.getId();
		UUID userId = UUID.randomUUID();;
		User user = new User();
		user.setId(userId);
		
		eventOne.enrollPlayer(user);
		
		when(eventRepo.getEventById(eventOne.getId())).thenThrow(NotFoundException.class);
		
		assertThatThrownBy(
				() -> eventService.removePlayerFromEvent(userId, eventOneId, "FOO")
			).isInstanceOf(NotFoundException.class);
	}
	
	@Test
	void removePlayerFromEventSholdThrowBadRequestExceptionIfPlayerCantBeFound() {
		UUID eventOneId = eventOne.getId();
		UUID userId = UUID.randomUUID();
		User user = new User();
		user.setId(userId);
		
		eventOne.enrollPlayer(user);
				
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(userService.getUserById(userId)).thenThrow(NotFoundException.class);
		

		assertThatThrownBy(
				() -> eventService.removePlayerFromEvent(userId, eventOneId, "FOO")
			).isInstanceOf(BadRequestException.class);
	}
	
	@Test
	void removePlayerShouldBeForbiddenIfRequesterIsNotTheAccountOwnerNorOneOfTheAdmins() {
		UUID eventOneId = eventOne.getId();
		UUID userId = UUID.randomUUID();
		User user = new User();
		user.setId(userId);
		
		eventOne.enrollPlayer(user);
				
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(userService.getUserById(userId)).thenReturn(user);
		
		doThrow(AccessDeniedException.class).when(authService).verifyOwnershipOf(user, "NotAuthUser");
		doThrow(AccessDeniedException.class).when(authService).verifyOwnershipOf(eventOne, "NotAuthUser");

		assertThatThrownBy(
				() -> eventService.removePlayerFromEvent(userId, eventOneId, "NotAuthUser")
			).isInstanceOf(AccessDeniedException.class);
	}
	
	@Test
	void removePlayerShouldBeAllowedIfRequesterIsNotTheAccountOwnerButIsOneOfTheAdmins() {
		UUID userId = UUID.randomUUID();
		User user = new User();
		user.setId(userId);
		
		eventOne.enrollPlayer(user);
				
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(userService.getUserById(userId)).thenReturn(user);
		
		doThrow(AccessDeniedException.class).when(authService).verifyOwnershipOf(user, "NotTheOwnerButAnAdmin");

		EventDetails result = eventService.removePlayerFromEvent(user.getId(), eventOne.getId(), "NotTheOwnerButAnAdmin");
		
		assertThat(result.getId()).isEqualTo(eventOne.getId());
		assertThat(result.getPlayerList()).doesNotContain(UserMapper.toUserBrief(user));
	}
	
	@Test
	void testGetJudgeListShouldReturnJudgeBriefList() {
		User judgeOne = new User(UUID.randomUUID());
		User judgeTwo = new User(UUID.randomUUID());
		eventOne.addJudge(judgeOne);
		eventOne.addJudge(judgeTwo);
		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		
		List<UserBrief> judgeList = eventService.getJudgeList(eventOne.getId()); 
		assertThat(judgeList).contains(
				UserMapper.toUserBrief(judgeOne), 
				UserMapper.toUserBrief(judgeTwo));
	}
	
	@Test
	void testGetJudgeListShouldReturnAnEmptyListIfNoJudgesArePresent() {
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		
		List<UserBrief> judgeList = eventService.getJudgeList(eventOne.getId()); 
		assertThat(judgeList).isEmpty();
	}
	
	@Test
	void testEnrollJudgeInEventShouldReturnJudgeBrief() {
		UUID judgeId = UUID.randomUUID();
		User judge = new User(judgeId);
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(userService.getUserById(judgeId)).thenReturn(judge);
		
		UserDetails returnedJudge = eventService.enrollJudgeInEvent(judgeId, eventOne.getId(), "ADMIN");
		
		assertThat(returnedJudge).isEqualTo(UserMapper.toUserDetails(judge));
		assertThat(returnedJudge.getId()).isEqualTo(judgeId);
		assertThat(eventOne.getJudgeList()).contains(judge);
	}
	
	@Test
	void testEnrollJudgeShouldThrowAnExceptionIfUserIsNotJudge() {
		UUID judgeId = UUID.randomUUID();
		UUID eventId = eventOne.getId();
		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(userService.getUserById(judgeId)).thenThrow(IllegalArgumentException.class);
		
		assertThatThrownBy(() -> eventService.enrollJudgeInEvent(judgeId, eventId, "ALLOWED"))
			.isInstanceOf(IllegalArgumentException.class);
		verify(authService).verifyOwnershipOf(eventOne, "ALLOWED");
	}
	
	@Test
	void testGetPlayerList() {
		User playerOne = new User(UUID.randomUUID());
		User playerTwo = new User(UUID.randomUUID());
		eventOne.enrollPlayer(playerOne);
		eventOne.enrollPlayer(playerTwo);
		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		
		List<UserBrief> playerList = eventService.getPlayersForEvent(eventOne.getId());
		
		assertThat(playerList).contains(UserMapper.toUserBrief(playerOne), UserMapper.toUserBrief(playerTwo));
	}

}
