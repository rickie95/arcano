package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

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
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Role;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.EventRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EventServiceTest {
	
	@Mock EventRepository eventRepo;
	@Mock UserService userService;
	@Mock AuthorizationService authService;
	
	@InjectMocks EventService eventService;
	
	@Test
	void testGetAllEventShouldRetrunAListOfEvents() {
		Event eventOne = new Event();
		eventOne.setId((long)1);
		
		Event eventTwo = new Event();
		eventTwo.setId((long) 2);
		
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
		Event eventOne = new Event();
		eventOne.setId((long)1);
		
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
	void testEnrollPlayerInAEvent() {
		User user = new User();
		user.setId((long)1);
		
		Event event = new Event((long)2);
		event.enrollPlayer(user);
		
		when(eventRepo.getEventById(event.getId())).thenReturn(Optional.of(event));
		
		EventDetails result = eventService.enrollPlayerInEvent(user.getId(), event.getId());
		
		assertThat(result.getId()).isEqualTo(event.getId());
		assertThat(result.getPlayerList()).contains(UserMapper.toUserBrief(user));
	}
	
	@Test
	void testEnrollPlayerInEventShouldThrowNotFoundExceptionIfUserCantBeFound() {
		Long userId = (long)1;
		User user = new User();
		user.setId(userId);
		
		Event event = new Event((long)2);
		
		when(eventRepo.getEventById(event.getId())).thenReturn(Optional.of(event));
		when(userService.getUserById(user.getId())).thenThrow(NotFoundException.class);
		
		assertThatThrownBy(
				() -> eventService.enrollPlayerInEvent(userId, (long)2)
			).isInstanceOf(NotFoundException.class);
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
	void testGetJudgeListShouldReturnJudgeBriefList() {
		Long eventId = (long) 1;
		Event event = new Event(eventId);
		User judgeOne = new User((long) 2);
		judgeOne.setRole(Role.JUDGE);
		User judgeTwo = new User((long) 3);
		judgeTwo.setRole(Role.JUDGE);
		event.addJudge(judgeOne);
		event.addJudge(judgeTwo);
		
		when(eventRepo.getEventById(eventId)).thenReturn(Optional.of(event));
		
		List<UserBrief> judgeList = eventService.getJudgeList(eventId); 
		assertThat(judgeList).contains(
				UserMapper.toUserBrief(judgeOne), 
				UserMapper.toUserBrief(judgeTwo));
	}
	
	@Test
	void testGetJudgeListShouldReturnAnEmptyListIfNoJudgesArePresent() {
		Long eventId = (long) 1;
		Event event = new Event(eventId);
		
		when(eventRepo.getEventById(eventId)).thenReturn(Optional.of(event));
		
		List<UserBrief> judgeList = eventService.getJudgeList(eventId); 
		assertThat(judgeList).isEmpty();
	}
	
	@Test
	void testEnrollJudgeInEventShouldReturnJudgeBrief() {
		Long judgeId = (long) 1;
		Long eventId = (long) 2;
		Event event = new Event(eventId);
		User judge = new User(judgeId);
		judge.setRole(Role.JUDGE);
		when(eventRepo.getEventById(eventId)).thenReturn(Optional.of(event));
		when(userService.getUserWithRoleById(judgeId, Role.JUDGE)).thenReturn(judge);
		
		UserDetails returnedJudge = eventService.enrollJudgeInEvent(judgeId, eventId, "ADMIN");
		
		assertThat(returnedJudge).isEqualTo(UserMapper.toUserDetails(judge));
		assertThat(returnedJudge.getId()).isEqualTo(judgeId);
		assertThat(event.getJudgeList()).contains(judge);
	}
	
	@Test
	void testEnrollJudgeShouldThrowAnExceptionIfUserIsNotJudge() {
		Long judgeId = (long) 1;
		Long eventId = (long) 2;
		Event event = new Event(eventId);
		
		when(eventRepo.getEventById(eventId)).thenReturn(Optional.of(event));
		when(userService.getUserWithRoleById(judgeId, Role.JUDGE)).thenThrow(IllegalArgumentException.class);
		
		assertThatThrownBy(() -> eventService.enrollJudgeInEvent(judgeId, eventId, "ALLOWED"))
			.isInstanceOf(IllegalArgumentException.class);
		verify(authService).verifyOwnershipOf(event, "ALLOWED");
	}

}
