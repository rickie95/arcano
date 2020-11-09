package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.MatchRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MatchServiceTest {

	private static final Long matchId = (long) 1;
	private static final Long playerOneId = (long) 1;
	private static final Long playerTwoId = (long) 2;

	@Mock MatchRepository matchRepo;
	@Mock UserService userService;
	@Mock AuthorizationService authService;

	@InjectMocks
	MatchService matchService;

	private User playerOne, playerTwo;
	private Match match;

	@BeforeEach
	void setUpPlayers() {
		playerOne = new User();
		playerTwo = new User();
		playerOne.setId(playerOneId);
		playerTwo.setId(playerTwoId);

		match = new Match();
		match.setId(matchId);
		match.setPlayerOne(playerOne);
		match.setPlayerTwo(playerTwo);
	}

	@Test
	void testCreateMatch() {
		when(matchRepo.addMatch(match)).thenReturn(match);
		when(userService.getUserById(playerOneId))
			.thenReturn(playerOne);
		when(userService.getUserById(playerTwoId))
			.thenReturn(playerTwo);
		
		Match createdMatch = matchService.createMatch(match);

		verify(matchRepo).addMatch(match);
		verify(userService).getUserById(playerOneId);
		verify(userService).getUserById(playerTwoId);
		
		assertThat(createdMatch).isEqualTo(match);
		assertThat(createdMatch.getPlayerOne()).isEqualTo(playerOne);
		assertThat(createdMatch.getPlayerTwo()).isEqualTo(playerTwo);

	}
	
	@Test
	void testGetAllMatches() {
		Match match2 = new Match();
		match2.setId((long)(2));
		
		List<Match> matchList = new ArrayList<>(Arrays.asList(match, match2));
		
		when(matchRepo.getAllMatches()).thenReturn(matchList);
		
		List<Match> returnedList = matchService.getAllMatches();
		
		verify(matchRepo).getAllMatches();
		assertThat(returnedList).contains(match, match2);
	}
	
	@Test
	void testGetMatchById() {
		when(matchRepo.getMatchById(matchId)).thenReturn(Optional.of(match));
		
		Match returnedMatch = matchService.getMatchById(matchId);
		
		assertThat(returnedMatch).isNotNull();
		assertThat(returnedMatch.getId()).isEqualTo(matchId);
	}
	
	@Test
	void testGetMatchByIdShouldThrowNotFoundExceptionIfMatchCannotBeFound() {
		when(matchRepo.getMatchById(matchId)).thenReturn(Optional.empty());
		
		assertThatThrownBy(
				() -> matchService.getMatchById(matchId))
			.isInstanceOf(NotFoundException.class);
	}
	
	@Test
	void testAddMatch() {
		when(matchRepo.addMatch(match)).thenReturn(match);
		
		Match returnedMatch = matchService.addMatch(match);
		
		assertThat(returnedMatch).isNotNull();
		assertThat(returnedMatch.getId()).isEqualTo(matchId);
	}
	
	@Test
	void testDeleteMatch() {
		String owner = "FOO";
		when(matchRepo.getMatchById(matchId)).thenReturn(Optional.of(match));
		when(matchRepo.removeMatch(match)).thenReturn(match);
		
		Match returnedMatch = matchService.deleteMatch(matchId, owner);
		
		verify(authService).verifyOwnershipOf(match, owner);
		assertThat(returnedMatch).isNotNull();
		assertThat(returnedMatch.getId()).isEqualTo(matchId);
	}
	
	@Test
	void testUpdateMatch() {
		String owner = "FOO";
		Match newMatch = new Match();
		newMatch.setPlayerOneScore(0);
		when(matchRepo.getMatchById(matchId)).thenReturn(Optional.of(match));
		when(matchRepo.updateMatch(newMatch)).thenReturn(newMatch);
		
		
		Match returnedMatch = matchService.updateMatch(matchId, newMatch, owner);
		
		verify(authService).verifyOwnershipOf(match, owner);
		assertThat(returnedMatch).isNotNull();
		assertThat(returnedMatch.getId()).isEqualTo(matchId);
		assertThat(returnedMatch.getPlayerOneScore()).isEqualByComparingTo((short)(0));
	}
	
	@Test
	void testGetMatchListForEvent() {
		Event event = new Event();
		event.setId((long)5);
		
		Match match2 = new Match();
		match2.setId((long)(2));
		match2.setParentEvent(event);
		
		match.setParentEvent(event);
		
		List<Match> matchList = new ArrayList<>(Arrays.asList(match, match2));
		
		when(matchRepo.getMatchForEvent(event.getId(), true)).thenReturn(matchList);
		
		List<Match> returnedList = matchService.getMatchListForEvent(event.getId(), true);
		
		verify(matchRepo).getMatchForEvent(event.getId(), true);
		assertThat(returnedList).contains(match, match2);
	}

}
