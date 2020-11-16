package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Role;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.persistence.EventDAO;
import com.riccardomalavolti.arcano.repositories.EventRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EventServiceRepositoryIT {
	
	EventService eventService;
	
	Event eventOne;
	Event eventTwo;
	
	@Mock EventDAO eventDAO;
	@Mock UserService userService;
	@Mock AuthorizationService authService;
	
	@InjectMocks
	EventRepository eventRepo;
	
	@BeforeEach
	void setUp() {
		eventService = new EventService(eventRepo, userService, authService);
		
		eventOne = new Event();
		eventOne.setId((long)1);
		
		eventTwo = new Event();
		eventTwo.setId((long) 2);
	}
	
	@Test
	void testGetAllEventShouldReturnListOfEvent() {
		List<Event> eventList = new ArrayList<Event>(Arrays.asList(eventOne, eventTwo));
		when(eventDAO.findAll()).thenReturn(eventList);
		
		List<EventBrief> returnedList = eventService.getAllEvents();
		
		assertThat(returnedList).contains(
				EventMapper.toEventBrief(eventOne),
				EventMapper.toEventBrief(eventTwo));
	}
	
	@Test
	void testGetEventById() {
		when(eventDAO.findById(eventOne.getId())).thenReturn(eventOne);
		EventDetails result = eventService.getEventById(eventOne.getId());
		assertThat(result.getId()).isEqualTo(eventOne.getId());
	}
	
	@Test
	void testEnrollPlayerInEvent() {
		User user = new User((long)(1));
		when(userService.getUserById(user.getId())).thenReturn(user);
		when(eventDAO.findById(eventOne.getId())).thenReturn(eventOne);
		
		EventDetails returnedEvent = eventService.enrollPlayerInEvent(user.getId(), eventOne.getId());
		
		assertThat(returnedEvent.getId()).isEqualTo(eventOne.getId());
		assertThat(returnedEvent.getPlayerList()).contains(UserMapper.toUserBrief(user));
	}
	
	@Test
	void testEnrollJudgeInEvent() {
		User user = new User((long)(1));
		user.setRole(Role.JUDGE);
		when(eventDAO.findById(eventOne.getId())).thenReturn(eventOne);
		when(userService.getUserWithRoleById(user.getId(), Role.JUDGE)).thenReturn(user);
		
		UserDetails returnedUser = eventService.enrollJudgeInEvent(user.getId(), eventOne.getId(), "ADMIN");
		
		assertThat(returnedUser.getId()).isEqualTo(user.getId());
	}
	
	@Test
	void testGetJudgeList() {
		User judgeOne = new User((long) 2);
		judgeOne.setRole(Role.JUDGE);
		User judgeTwo = new User((long) 3);
		judgeTwo.setRole(Role.JUDGE);
		eventOne.addJudge(judgeOne);
		eventOne.addJudge(judgeTwo);
		
		when(eventDAO.findById(eventOne.getId())).thenReturn(eventOne);
		
		List<UserBrief> judgeList = eventService.getJudgeList(eventOne.getId());
		
		assertThat(judgeList).contains(
				UserMapper.toUserBrief(judgeOne), 
				UserMapper.toUserBrief(judgeTwo));
	}
	
	@Test
	void testPlayerList() {
		User playerOne = new User((long) 2);
		playerOne.setRole(Role.PLAYER);
		User playerTwo = new User((long) 3);
		playerTwo.setRole(Role.PLAYER);
		eventOne.enrollPlayer(playerOne);
		eventOne.enrollPlayer(playerTwo);
		
		when(eventDAO.findById(eventOne.getId())).thenReturn(eventOne);
		
		List<UserBrief> playerList = eventService.getPlayersForEvent(eventOne.getId());
		
		assertThat(playerList).contains(
				UserMapper.toUserBrief(playerOne), 
				UserMapper.toUserBrief(playerTwo));
	}

}
