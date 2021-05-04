package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.dto.MatchBrief;
import com.riccardomalavolti.arcano.dto.MatchDetails;
import com.riccardomalavolti.arcano.dto.MatchMapper;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.MatchRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MatchServiceTest {

	private static final UUID matchId = UUID.randomUUID();
	private static final UUID playerOneId = UUID.randomUUID();
	private static final UUID playerTwoId = UUID.randomUUID();

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
		
		MatchDetails createdMatch = matchService.createMatch(match);

		verify(matchRepo).addMatch(match);
		verify(userService).getUserById(playerOneId);
		verify(userService).getUserById(playerTwoId);
		
		assertThat(createdMatch.getId()).isEqualTo(matchId);
		assertThat(createdMatch.getPlayerOne().getId()).isEqualTo(playerOneId);
		assertThat(createdMatch.getPlayerTwo().getId()).isEqualTo(playerTwoId);

	}
	
	@Test
	void testGetAllMatches() {
		Match match2 = new Match();
		match2.setId(UUID.randomUUID());
		
		List<Match> matchList = new ArrayList<>(Arrays.asList(match, match2));
		
		when(matchRepo.getAllMatches()).thenReturn(matchList);
		
		List<MatchBrief> returnedList = matchService.getAllMatches();
		
		verify(matchRepo).getAllMatches();
		assertThat(returnedList).contains(MatchMapper.toMatchBrief(match), MatchMapper.toMatchBrief(match2));
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
		
		MatchDetails returnedMatch = matchService.addMatch(match);
		
		assertThat(returnedMatch).isNotNull();
		assertThat(returnedMatch.getId()).isEqualTo(matchId);
	}
	
	@Test
	void testDeleteMatch() {
		String owner = "FOO";
		when(matchRepo.getMatchById(matchId)).thenReturn(Optional.of(match));
		when(matchRepo.removeMatch(match)).thenReturn(match);
		
		MatchDetails returnedMatch = matchService.deleteMatch(matchId, owner);
		
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
		
		
		MatchDetails returnedMatch = matchService.updateMatch(matchId, newMatch, owner);
		
		verify(authService).verifyOwnershipOf(match, owner);
		assertThat(returnedMatch).isNotNull();
		assertThat(returnedMatch.getId()).isEqualTo(matchId);
		assertThat(returnedMatch.getPlayerOneScore()).isEqualByComparingTo((short)(0));
	}
	
	@Test
	void testGetMatchListForEvent() {
		Event event = new Event();
		event.setId(UUID.randomUUID());
		
		Match match2 = new Match();
		match2.setId(UUID.randomUUID());
		match2.setParentEvent(event);
		
		match.setParentEvent(event);
		
		List<Match> matchList = new ArrayList<>(Arrays.asList(match, match2));
		
		when(matchRepo.getMatchForEvent(event.getId())).thenReturn(matchList);
		
		List<MatchDetails> returnedList = matchService.getMatchListForEvent(event.getId());
		
		verify(matchRepo).getMatchForEvent(event.getId());
		assertThat(returnedList).contains(MatchMapper.toMatchDetails(match), MatchMapper.toMatchDetails(match2));
	}

}
