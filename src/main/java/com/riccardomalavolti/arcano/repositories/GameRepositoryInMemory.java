package com.riccardomalavolti.arcano.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.inject.Default;
import javax.inject.Singleton;

import com.riccardomalavolti.arcano.exceptions.ConflictException;
import com.riccardomalavolti.arcano.model.Game;

@Default
@Singleton
public class GameRepositoryInMemory implements GameRepository {
	
	private static Map<Long, Game> gameList = new HashMap<>();
	private static Long gameIdCounter = (long) 0;

	@Override
	public synchronized Game createGame(Game game) {		
		game.setId(gameIdCounter++);
		gameList.put(game.getId(), game);
		return game;
	}
	
	@Override
	public synchronized Game addGame(Game game) {
		if(gameList.put(game.getId(), game) == null)
			return game;
		
		throw new ConflictException(String.format("Game with id %s is already present.", game.getId()));
	}

	@Override
	public Optional<Game> findGameById(Long gameId) {
		return Optional.of(gameList.get(gameId));
	}

	@Override
	public List<Game> findAllGames() {
		return new ArrayList<>(gameList.values());
	}

	@Override
	public synchronized Game removeGame(Game game) {
		return gameList.remove(game.getId());
	}

	public static Long getIdCounter() {
		return GameRepositoryInMemory.gameIdCounter;
	}

}
