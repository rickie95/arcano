package com.riccardomalavolti.arcano.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.riccardomalavolti.arcano.dto.EventBrief;
import com.riccardomalavolti.arcano.dto.EventDetails;
import com.riccardomalavolti.arcano.dto.EventMapper;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.EventStatus;
import com.riccardomalavolti.arcano.model.User;

@RunWith(JUnitPlatform.class)
class EventMapperTest {
	
	EventMapper mapper;
	
	Event event;
	UUID eventId;
	String eventName;
	
	User userOne;
	User userTwo;
	
	@BeforeEach
	void createNewEvent() {
		eventId = UUID.randomUUID();
		eventName = "FOO";
		event = new Event(eventId, eventName);
		
		userOne = new User(UUID.randomUUID());
		userTwo = new User(UUID.randomUUID());
		
		List<User> playerList = new ArrayList<>(Arrays.asList(userOne, userTwo));
		List<User> judgeList = new ArrayList<>(Arrays.asList(userOne, userTwo));
		List<User> adminList = new ArrayList<>(Arrays.asList(userOne, userTwo));
		
		event.setPlayerList(new HashSet<User>(playerList));
		event.setAdminList(new HashSet<User>(judgeList));
		event.setJudgeList(new HashSet<User>(adminList));
		
		event.setStatus(EventStatus.IN_PROGRESS);
		event.setType("Limited");
		event.setRound(0);
		event.setStartingTime(LocalDateTime.of(2021, 2, 5, 12, 45));
	}
	
	@Test
	void callingPrivateConstructorThrowsUnsuppordedException() {
		final Constructor<?>[] constructors = EventMapper.class.getDeclaredConstructors();
		assertThat(constructors).hasSize(1);
		assertThrows(IllegalAccessException.class, () -> constructors[0].newInstance());
	}
	
	@Test
	void testToEventBrief() {
		EventBrief eventBrief = EventMapper.toEventBrief(event);
		eventBrief.addUri("localhost/something");
		
		assertThat(eventBrief.getId()).isEqualTo(eventId);
		assertThat(eventBrief.getName()).isEqualTo(eventName);
		assertThat(eventBrief.getUri()).isEqualTo(eventBrief.getUri());
	}
	
	@Test
	void testToEventDetails() {
		EventDetails eventDetails = EventMapper.toEventDetails(event);
		
		assertThat(eventDetails.getId()).isEqualTo(eventId);
		assertThat(eventDetails.getName()).isEqualTo(eventName);
		assertThat(eventDetails.getStatus()).isEqualTo(EventStatus.IN_PROGRESS);
		assertThat(eventDetails.getRound()).isEqualTo(event.getRound());
		assertThat(eventDetails.getType()).isEqualTo(event.getType());
		
		assertThat(eventDetails.getPlayerList()).contains(
				UserMapper.toUserBrief(userOne),
				UserMapper.toUserBrief(userTwo));
		
		assertThat(eventDetails.getPlayerList()).contains(
				UserMapper.toUserBrief(userOne),
				UserMapper.toUserBrief(userTwo));
		
		assertThat(eventDetails.getAdminList()).contains(
				UserMapper.toUserBrief(userOne),
				UserMapper.toUserBrief(userTwo));
		
		assertThat(eventDetails.getJudgeList()).contains(
				UserMapper.toUserBrief(userOne),
				UserMapper.toUserBrief(userTwo));
		
		assertThat(eventDetails.getStartingTime())
			.isEqualTo(event.getStartingTime());
		
	}	

}
