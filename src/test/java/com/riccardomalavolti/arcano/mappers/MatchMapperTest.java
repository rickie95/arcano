package com.riccardomalavolti.arcano.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.riccardomalavolti.arcano.dto.EventMapper;
import com.riccardomalavolti.arcano.dto.MatchBrief;
import com.riccardomalavolti.arcano.dto.MatchMapper;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;

@RunWith(JUnitPlatform.class)
class MatchMapperTest {
	
	
	Match match;
	UUID matchId;

	User userOne;
	User userTwo;

	UUID eventId;
	String eventName;
	Event event;
	
	
	@BeforeEach
	void createNewEvent() {
		matchId = UUID.randomUUID();
		match = new Match(matchId);
		
		userOne = new User(UUID.randomUUID());
		userTwo = new User(UUID.randomUUID());
		
		match.setPlayerOne(userOne);
		match.setPlayerTwo(userTwo);
		
		match.setPlayerOneScore(1);
		match.setPlayerTwoScore(2);
		match.setMatchEnded(true);
		
		eventId = UUID.randomUUID();
		eventName = "FOO";
		event = new Event(eventId, eventName);
		match.setParentEvent(event);
	}
	
	@Test
	void testToMatchDTO() {
		MatchBrief matchDto = MatchMapper.toMatchBrief(match);
		
		assertThat(matchDto.getId()).isEqualTo(matchId);
		assertThat(matchDto.isMatchEnded()).isTrue();
		assertThat(matchDto.getUserOne()).isEqualTo(UserMapper.toUserBrief(userOne));
		assertThat(matchDto.getUserTwo()).isEqualTo(UserMapper.toUserBrief(userTwo));
		assertThat(matchDto.getPlayerOneScore()).isEqualTo((short)1);
		assertThat(matchDto.getPlayerTwoScore()).isEqualTo((short)2);
		assertThat(matchDto.getParentEvent()).isEqualTo(EventMapper.toEventBrief(event));
	}

}
