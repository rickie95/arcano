package com.riccardomalavolti.arcano.repositories;


import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Player;

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
		Player playerOne = new Player();
		playerOne.setId(p1ID);
		
		Long p2ID = (long) 2;
		Player playerTwo = new Player();
		playerTwo.setId(p2ID);
		
		Game g = gameRepo.createGame(playerOne, playerTwo);
		
		assertThat(gameRepo.findGameById(g.getId())).isEqualTo(g);
	}
	
	@Test
	void testFindAllGamesShouldReturnsAllGames() {
		Long p1ID = (long) 1;
		Player playerOne = new Player();
		playerOne.setId(p1ID);
		
		Long p2ID = (long) 2;
		Player playerTwo = new Player();
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
		Player playerOne = new Player();
		playerOne.setId(p1ID);
		
		Long p2ID = (long) 2;
		Player playerTwo = new Player();
		playerTwo.setId(p2ID);
		
		Game gameOne = gameRepo.createGame(playerOne, playerTwo);
		
		gameRepo.removeGame(gameOne);
		
		assertThat(gameRepo.findGameById(gameOne.getId())).isNull();
	}
	

}
