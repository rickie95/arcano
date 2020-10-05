package com.riccardomalavolti.arcano.repositories;


import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.User;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class GameRepositoryInMemoryTest {
	
	GameRepositoryInMemory gameRepo;
	
	@BeforeEach
	void setUp() {
		gameRepo = new GameRepositoryInMemory();
	}
	
	@Test
	void testCreateAGameShouldReturnTheGameItself() {
		Long p1ID = (long) 1;
		User playerOne = new User();
		playerOne.setId(p1ID);
		
		Long p2ID = (long) 2;
		User playerTwo = new User();
		playerTwo.setId(p2ID);
		
		Game g = gameRepo.createGame(playerOne, playerTwo);
		
		Optional<Game> returnedGame = gameRepo.findGameById(g.getId());
		
		assertThat(returnedGame).isNotEmpty().contains(g);
	}
	
	@Test
	void testFindAllGamesShouldReturnsAllGames() {
		Long p1ID = (long) 1;
		User playerOne = new User();
		playerOne.setId(p1ID);
		
		Long p2ID = (long) 2;
		User playerTwo = new User();
		playerTwo.setId(p2ID);
		
		Game gameOne = gameRepo.createGame(playerOne, playerTwo);
		Game gameTwo = gameRepo.createGame(playerOne, playerTwo);
		Game gameThree = gameRepo.createGame(playerOne, playerTwo);
		
		assertThat(gameRepo.findAllGames())
			.contains(gameOne, gameTwo, gameThree);
	}
	
	@Test
	void testRemoveGame() {
		Long p1ID = (long) 1;
		
		Long p2ID = (long) 2;
		
		Game gameOne = new Game((long)(3));
		gameOne.setPointsForPlayer(p1ID, (short)(20));
		gameOne.setPointsForPlayer(p2ID, (short)(20));
		
		gameRepo.addGame(gameOne);
		
		Game returned = gameRepo.removeGame(gameOne);
		
		assertThat(returned).isNotNull().isEqualTo(gameOne);
	}
	

}
