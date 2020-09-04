package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.MatchRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MatchServiceTest {

	private static final Long matchId = (long) 1;
	private static final Long playerOneId = (long) 1;
	private static final Long playerTwoId = (long) 2;

	@Mock
	MatchRepository matchRepo;

	@InjectMocks
	MatchService matchService;

	private Player playerOne, playerTwo;
	private Match match;

	@BeforeEach
	void setUpPlayers() {
		playerOne = new Player();
		playerTwo = new Player();
		playerOne.setId(playerOneId);
		playerTwo.setId(playerTwoId);

		match = new Match();
		match.setId(matchId);
	}

	@Test
	void testCreateMatch() {
		match.setPlayerOne(playerOne);
		match.setPlayerTwo(playerTwo);
		when(matchRepo.addMatch(any(Match.class))).thenReturn(match);
		Match createdMatch = matchService.createMatch(playerOne, playerTwo);

		verify(matchRepo).addMatch(any(Match.class));

		assertThat(createdMatch).isEqualTo(match);

	}

}
