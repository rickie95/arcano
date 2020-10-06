package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.MatchRepository;
import com.riccardomalavolti.arcano.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MatchServiceTest {

	private static final Long matchId = (long) 1;
	private static final Long playerOneId = (long) 1;
	private static final Long playerTwoId = (long) 2;

	@Mock MatchRepository matchRepo;
	@Mock UserRepository playerRepo;

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
		when(playerRepo.getUserById(playerOneId))
			.thenReturn(Optional.of(playerOne));
		when(playerRepo.getUserById(playerTwoId))
			.thenReturn(Optional.of(playerTwo));
		
		Match createdMatch = matchService.createMatch(match);

		verify(matchRepo).addMatch(match);
		verify(playerRepo).getUserById(playerOneId);
		verify(playerRepo).getUserById(playerTwoId);
		
		assertThat(createdMatch).isEqualTo(match);

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

}
