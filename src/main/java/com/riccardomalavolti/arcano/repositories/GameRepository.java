package com.riccardomalavolti.arcano.repositories;

import java.util.List;
import java.util.Optional;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Match;

public interface GameRepository {

	public Game createGame(Game game);

	public Optional<Game> findGameById(Long gameid);

	public List<Game> findAllGames();
	
	public Game removeGame(Game game);

	public Game addGame(Game game);

    public List<Game> getGameListForMatch(Match m);
		
}
