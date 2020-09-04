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
	public Game createGame(Player playerOne, Player playerTwo) {
		Game game = new Game(gameIdCounter++, playerOne, playerTwo);
		gameList.put(game.getId(), game);
		return game;
	}

	@Override
	public Game findGameById(Long gameid) {
		return gameList.get(gameid);
	}

	@Override
	public List<Game> findAllGames() {
		return new ArrayList<>(gameList.values());
	}

	@Override
	public void removeGame(Game game) {
		gameList.remove(game.getId());
	}

}
