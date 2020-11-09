package com.riccardomalavolti.arcano.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.riccardomalavolti.arcano.dto.EventMapper;
import com.riccardomalavolti.arcano.dto.MatchDTO;
import com.riccardomalavolti.arcano.dto.MatchMapper;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;

@RunWith(JUnitPlatform.class)
class MatchMapperTest {
	
	
	Match match;
	Long matchId;

	User userOne;
	User userTwo;

	Long eventId;
	String eventName;
	Event event;
	
	
	@BeforeEach
	void createNewEvent() {
		matchId = (long) 1;
		match = new Match(matchId);
		
		userOne = new User((long) 2);
		userTwo = new User((long) 3);
		
		match.setPlayerOne(userOne);
		match.setPlayerTwo(userTwo);
		
		match.setPlayerOneScore(1);
		match.setPlayerTwoScore(2);
		match.setMatchEnded(true);
		
		eventId = (long) 2;
		eventName = "FOO";
		event = new Event(eventId, eventName);
		match.setParentEvent(event);
	}
	
	@Test
	void testToMatchDTO() {
		MatchDTO matchDto = MatchMapper.toMatchDTO(match);
		
		assertThat(matchDto.getId()).isEqualTo(matchId);
		assertThat(matchDto.isMatchEnded()).isTrue();
		assertThat(matchDto.getUserOne()).isEqualTo(UserMapper.toUserDTO(userOne));
		assertThat(matchDto.getUserTwo()).isEqualTo(UserMapper.toUserDTO(userTwo));
		assertThat(matchDto.getPlayerOneScore()).isEqualTo((short)1);
		assertThat(matchDto.getPlayerTwoScore()).isEqualTo((short)2);
		assertThat(matchDto.getParentEvent()).isEqualTo(EventMapper.toEventDTO(event));
	}

}
