package com.riccardomalavolti.arcano.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Default;
import javax.inject.Singleton;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Player;

@Default
@Singleton
public class GameRepositoryInMemory implements GameRepository {
	
	private Map<Long, Game> gameList;
	private Long gameIdCounter;
	
	public GameRepositoryInMemory() {
		gameList = new HashMap<>();
		gameIdCounter = (long) 0;
	}

	@Override
	public synchronized Game createGame(Player playerOne, Player playerTwo) {
		/*
		 *  TODO: renderlo più generico, permettendo più giocatori 
		 *  e diversi punteggi di partenza.
		 */
		
		Game game = new Game(gameIdCounter++);
		game.setPointsForPlayer(playerOne.getId(), (short) 20);
		game.setPointsForPlayer(playerTwo.getId(), (short) 20);
		gameList.put(game.getId(), game);
		return game;
	}

	@Override
	public Game findGameById(Long gameId) {
		return gameList.get(gameId);
	}

	@Override
	public List<Game> findAllGames() {
		return new ArrayList<>(gameList.values());
	}

	@Override
	public synchronized void removeGame(Game game) {
		gameList.remove(game.getId());
	}

}
