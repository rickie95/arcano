package com.riccardomalavolti.arcano.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.riccardomalavolti.arcano.dto.EventDTO;
import com.riccardomalavolti.arcano.dto.EventDetails;
import com.riccardomalavolti.arcano.dto.EventMapper;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.User;

@RunWith(JUnitPlatform.class)
class EventMapperTest {
	
	EventMapper mapper;
	
	Event event;
	Long eventId;
	String eventName;
	
	User userOne;
	User userTwo;
	
	@BeforeEach
	void createNewEvent() {
		eventId = (long) 1;
		eventName = "FOO";
		event = new Event(eventId, eventName);
		
		userOne = new User((long) 2);
		userTwo = new User((long) 3);
		
		List<User> playerList = new ArrayList<>(Arrays.asList(userOne, userTwo));
		List<User> judgeList = new ArrayList<>(Arrays.asList(userOne, userTwo));
		List<User> adminList = new ArrayList<>(Arrays.asList(userOne, userTwo));
		
		event.setPlayerList(new HashSet<User>(playerList));
		event.setAdminList(new HashSet<User>(judgeList));
		event.setJudgeList(new HashSet<User>(adminList));
	}
	
	@Test
	void testToEventDTO() {
		EventDTO eventDto = EventMapper.toEventDTO(event);
		
		assertThat(eventDto.getId()).isEqualTo(eventId);
		assertThat(eventDto.getName()).isEqualTo(eventName);
	}
	
	@Test
	void testToEventDetails() {
		EventDetails eventDetails = EventMapper.toEventDetails(event);
		
		assertThat(eventDetails.getId()).isEqualTo(eventId);
		assertThat(eventDetails.getName()).isEqualTo(eventName);
		assertThat(eventDetails.getPlayerList()).contains(
				UserMapper.toUserDTO(userOne),
				UserMapper.toUserDTO(userTwo));
		
		assertThat(eventDetails.getPlayerList()).contains(
				UserMapper.toUserDTO(userOne),
				UserMapper.toUserDTO(userTwo));
		
		assertThat(eventDetails.getAdminList()).contains(
				UserMapper.toUserDTO(userOne),
				UserMapper.toUserDTO(userTwo));
		
		assertThat(eventDetails.getJudgeList()).contains(
				UserMapper.toUserDTO(userOne),
				UserMapper.toUserDTO(userTwo));
		
	}	

}
