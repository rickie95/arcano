package com.riccardomalavolti.arcano.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.GameRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class GameServiceTest {

	private static final Long gameId = (long) 1;
	private static final Long playerOneId = (long) 1;
	private static final Long playerTwoId = (long) 2;

	@Mock
	private GameRepository gameRepository;
	@Mock
	private Game mockedGame;
	
	@InjectMocks
	private GameService gameService;
	
	private Player playerOne, playerTwo;
	private Game game;
	
	@BeforeEach
	void setUpPlayers() {
		playerOne = new Player();
		playerTwo = new Player();
		playerOne.setId(playerOneId);
		playerTwo.setId(playerTwoId);
		
		game = new Game(gameId);
	}

	@Test
	void testCreateNewGame() {
		when(gameRepository.createGame(playerOne, playerTwo)).thenReturn(game);
		
		Game createdGame = gameService.createGame(playerOne, playerTwo);
		
		verify(gameRepository).createGame(playerOne, playerTwo);
		assertEquals(game, createdGame);
	}
	
	@Test
	void testGetGameById() {
		when(gameRepository.findGameById(gameId)).thenReturn(game);
		
		Game returnedGame = gameService.getGameById(gameId);
		
		verify(gameRepository).findGameById(gameId);
		assertEquals(game, returnedGame);
	}
	
	@Test
	void testGetAllGames() {
		Game game2 = new Game((long)2);
		List<Game> gameList = new ArrayList<>(Arrays.asList(game, game2));
		
		when(gameRepository.findAllGames()).thenReturn(gameList);
		
		List<Game> returnedGameList = gameService.allGames();
		
		assertThat(returnedGameList).contains(game, game2);
	}
	
}
