package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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

import com.riccardomalavolti.arcano.dto.UserDetails;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.GameRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class GameServiceTest {

	private static final Long gameId = (long) 1;
	private static final Long playerOneId = (long) 1;
	private static final Long playerTwoId = (long) 2;

	@Mock private UserService userService;
	@Mock private GameRepository gameRepository;
	@Mock private Game mockedGame;
	
	@InjectMocks
	private GameService gameService;
	
	private User playerOne, playerTwo;
	private Game game;
	
	@BeforeEach
	void setUpPlayers() {
		playerOne = new User();
		playerTwo = new User();
		playerOne.setId(playerOneId);
		playerTwo.setId(playerTwoId);
		
		game = new Game(gameId);
	}

	@Test
	void testCreateNewGame() {
		when(gameRepository.createGame(playerOne, playerTwo)).thenReturn(game);
		
		Game createdGame = gameService.createGame(playerOne, playerTwo);
		
		verify(gameRepository).createGame(playerOne, playerTwo);
		assertThat(game).isEqualTo(createdGame);
	}
	
	@Test
	void testGetGameById() {
		when(gameRepository.findGameById(gameId)).thenReturn(Optional.of(game));
		
		Game returnedGame = gameService.getGameById(gameId);
		
		verify(gameRepository).findGameById(gameId);
		assertThat(returnedGame).isEqualTo(game);
	}
	
	@Test
	void testGetGameByIdShouldThrowNotFoundException() {
		when(gameRepository.findGameById(gameId)).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> gameService.getGameById(gameId));
	}
	
	@Test
	void testGetAllGames() {
		Game game2 = new Game((long)2);
		List<Game> gameList = new ArrayList<>(Arrays.asList(game, game2));
		
		when(gameRepository.findAllGames()).thenReturn(gameList);
		
		List<Game> returnedGameList = gameService.allGames();
		
		assertThat(returnedGameList).contains(game, game2);
	}
	
	@Test
	void testSetPointsForPlayerInGame() {
		Long playerId = (long) 2;
		Short points = (short) 3;
		
		Game game = mock(Game.class);
		when(gameRepository.findGameById(gameId)).thenReturn(Optional.of(game));
		
		gameService.setPointsForPlayerInGame(gameId, playerId, points);
		
		verify(game).setPointsForPlayer(playerId, points);
	}
	
	@Test
	void testGetPointForPlayerInGame() {
		Long playerId = (long) 2;
		Short points = (short) 3;
		
		Game game = mock(Game.class);
		when(gameRepository.findGameById(gameId)).thenReturn(Optional.of(game));
		when(game.getPointsForPlayer(playerId)).thenReturn(points);
		
		Short returnedPoint = gameService.getPointForPlayerInGame(playerId, gameId);
		
		assertThat(returnedPoint).isEqualTo(points);
	}
	
	@Test
	void testGetOpponentForPlayerInGame() {
		Long playerId = (long) 2;
		Long opponentId = (long) 3;
		User opponent = new User();
		opponent.setId(opponentId);
		Game game = mock(Game.class);
		
		when(gameRepository.findGameById(gameId)).thenReturn(Optional.of(game));
		when(game.opponentOf(playerId)).thenReturn(opponent.getId());
		
		Long returnedId = gameService.getOpponentIdForPlayerInGame(playerId, gameId);
		
		assertThat(returnedId).isEqualTo(opponentId);
	}

	@Test
	void testGetWinnerOfGame() {
		Long winnerId = (long) 4;
		User winner = new User();
		winner.setId(winnerId);
		
		when(gameRepository.findGameById(gameId)).thenReturn(Optional.of(mockedGame));
		when(mockedGame.getWinnerId()).thenReturn(winnerId);
		when(userService.getUserDetailsById(winnerId)).thenReturn(UserMapper.toUserDetails(winner));
		
		UserDetails returnedPlayer = gameService.getWinnerOfGame(gameId);
		
		assertThat(returnedPlayer.getId()).isEqualTo(winner.getId());
	}
	
	@Test
	void testWithdrawPlayer() {
		Game mockedGame = mock(Game.class);
		when(gameRepository.findGameById(gameId)).thenReturn(Optional.of(mockedGame));
		
		gameService.withdrawPlayer(gameId, playerOneId);
		
		verify(mockedGame).withdrawPlayer(playerOneId);
		verify(mockedGame).endGame();
		
	}
}
