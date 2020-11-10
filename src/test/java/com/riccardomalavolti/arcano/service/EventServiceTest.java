package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.model.Event;
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
		
		when(eventRepo.getEventById(event.getId())).thenReturn(Optional.of(event));
		when(userService.getUserById(user.getId())).thenReturn(user);
		
		EventDetails result = eventService.enrollPlayerInEvent(user.getId(), event.getId());
		
		assertThat(result.getId()).isEqualTo(event.getId());
		assertThat(result.getPlayerList()).contains(UserMapper.toUserBrief(user));
	}
	
	@Test
	void testEnrollPlayerInEventShouldThrowNotFoundExceptionIfUserCantBeFound() {
		Long userId = (long)1;
		User user = new User();
		user.setId(userId);
		
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
		
		when(userService.getUserById(user.getId())).thenReturn(user);
		when(eventRepo.getEventById((long)2)).thenThrow(NotFoundException.class);
		
		assertThatThrownBy(
				() -> eventService.enrollPlayerInEvent(userId, (long)2)
			).isInstanceOf(NotFoundException.class);
		
	}

}
