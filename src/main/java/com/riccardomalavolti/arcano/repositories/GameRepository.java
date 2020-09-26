package com.riccardomalavolti.arcano.repositories;

import java.util.List;
import java.util.Optional;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Player;

public interface GameRepository {

	public Game createGame(Player playerOne, Player playerTwo);

	public Optional<Game> findGameById(Long gameid);

	public List<Game> findAllGames();
	
	public Game removeGame(Game game);

	public Game addGame(Game game);
		
}
