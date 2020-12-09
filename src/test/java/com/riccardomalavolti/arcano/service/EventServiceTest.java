package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import com.riccardomalavolti.arcano.model.Role;
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
		eventOne = new Event((long)1);
		eventTwo = new Event((long) 2);
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
		when(eventRepo.getEventById(anyLong())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> 
				eventService.getEventById((long) 1)
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
		user.setId((long)1);
		
		eventOne.enrollPlayer(user);
		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		
		EventDetails result = eventService.enrollPlayerInEvent(user.getId(), eventOne.getId());
		
		assertThat(result.getId()).isEqualTo(eventOne.getId());
		assertThat(result.getPlayerList()).contains(UserMapper.toUserBrief(user));
	}
	
	@Test
	void testEnrollPlayerInEventShouldThrowNotFoundExceptionIfUserCantBeFound() {
		Long eventId = eventOne.getId();
		Long userId = (long)1;
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
		Long userId = (long)1;
		User user = new User();
		user.setId(userId);
		
		when(eventRepo.getEventById((long)2)).thenThrow(NotFoundException.class);
		
		assertThatThrownBy(
				() -> eventService.enrollPlayerInEvent(userId, (long)2)
			).isInstanceOf(NotFoundException.class);
	}
	
	@Test
	void removePlayerFromEvent() {
		Long userId = (long)1;
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
		Long eventOneId = eventOne.getId();
		Long userId = (long)1;
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
		Long eventOneId = eventOne.getId();
		Long userId = (long)1;
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
		Long eventOneId = eventOne.getId();
		Long userId = (long)1;
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
		Long userId = (long)1;
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
		User judgeOne = new User((long) 2);
		judgeOne.setRole(Role.JUDGE);
		User judgeTwo = new User((long) 3);
		judgeTwo.setRole(Role.JUDGE);
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
		Long judgeId = (long) 1;
		User judge = new User(judgeId);
		judge.setRole(Role.JUDGE);
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(userService.getUserWithRoleById(judgeId, Role.JUDGE)).thenReturn(judge);
		
		UserDetails returnedJudge = eventService.enrollJudgeInEvent(judgeId, eventOne.getId(), "ADMIN");
		
		assertThat(returnedJudge).isEqualTo(UserMapper.toUserDetails(judge));
		assertThat(returnedJudge.getId()).isEqualTo(judgeId);
		assertThat(eventOne.getJudgeList()).contains(judge);
	}
	
	@Test
	void testEnrollJudgeShouldThrowAnExceptionIfUserIsNotJudge() {
		Long judgeId = (long) 1;
		Long eventId = eventOne.getId();
		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		when(userService.getUserWithRoleById(judgeId, Role.JUDGE)).thenThrow(IllegalArgumentException.class);
		
		assertThatThrownBy(() -> eventService.enrollJudgeInEvent(judgeId, eventId, "ALLOWED"))
			.isInstanceOf(IllegalArgumentException.class);
		verify(authService).verifyOwnershipOf(eventOne, "ALLOWED");
	}
	
	@Test
	void testGetPlayerList() {
		User playerOne = new User((long) 2);
		playerOne.setRole(Role.PLAYER);
		User playerTwo = new User((long) 3);
		playerTwo.setRole(Role.PLAYER);
		eventOne.enrollPlayer(playerOne);
		eventOne.enrollPlayer(playerTwo);
		
		when(eventRepo.getEventById(eventOne.getId())).thenReturn(Optional.of(eventOne));
		
		List<UserBrief> playerList = eventService.getPlayersForEvent(eventOne.getId());
		
		assertThat(playerList).contains(UserMapper.toUserBrief(playerOne), UserMapper.toUserBrief(playerTwo));
	}

}
