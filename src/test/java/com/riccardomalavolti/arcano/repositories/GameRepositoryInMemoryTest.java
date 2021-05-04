package com.riccardomalavolti.arcano.repositories;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.exceptions.ConflictException;
import com.riccardomalavolti.arcano.model.Game;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class GameRepositoryInMemoryTest {
	
	GameRepositoryInMemory gameRepo;
	
	private static UUID playerOneId = UUID.randomUUID();
	private static UUID playerTwoId = UUID.randomUUID();
	
	@BeforeEach
	void setUp() {
		gameRepo = new GameRepositoryInMemory();
		gameRepo.findAllGames().forEach(game -> gameRepo.removeGame(game));
	}
	
	@Test
	void testCreateAGameShouldReturnTheGameItself() {		
		Game newGame = new Game();
		newGame.setPointsForPlayer(playerOneId, (short)(20));
		newGame.setPointsForPlayer(playerTwoId, (short)(20));
		
		Game g = gameRepo.createGame(newGame);
		
		Optional<Game> returnedGame = gameRepo.findGameById(g.getId());
		
		assertThat(returnedGame).isNotEmpty().contains(g);
		assertThat(returnedGame.get().getId()).isEqualTo(GameRepositoryInMemory.getIdCounter()-1);
		assertThat(returnedGame.get().getPointsForPlayer(playerOneId)).isEqualTo((short)(20));
		assertThat(returnedGame.get().getPointsForPlayer(playerTwoId)).isEqualTo((short)(20));
	}
	
	@Test
	void testAddGameShouldBeIdempotent() {		
		Game game = new Game((long)(1));
		Game returnedGame = gameRepo.addGame(game);
		
		assertThat(returnedGame).isNotNull();
		assertThat(returnedGame.getId()).isEqualTo((long)(1));
		
		assertThrows(ConflictException.class, () -> gameRepo.addGame(game));
	}
	
	
	@Test
	void testFindAllGamesShouldReturnsAllGames() {
		Game newGame = new Game();
		newGame.setPointsForPlayer(playerOneId, (short)20);
		newGame.setPointsForPlayer(playerTwoId, (short)20);
		
		Game gameOne = gameRepo.createGame(newGame);
		Game gameTwo = gameRepo.createGame(newGame);
		Game gameThree = gameRepo.createGame(newGame);
		
		assertThat(gameRepo.findAllGames())
			.contains(gameOne, gameTwo, gameThree);
	}
	
	@Test
	void testRemoveGame() {
		Game gameOne = new Game((long)(3));
		gameOne.setPointsForPlayer(playerOneId, (short)(20));
		gameOne.setPointsForPlayer(playerTwoId, (short)(20));
		
		gameRepo.addGame(gameOne);
		
		Game returned = gameRepo.removeGame(gameOne);
		
		assertThat(returned).isNotNull().isEqualTo(gameOne);
	}
	

}
