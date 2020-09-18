package com.riccardomalavolti.arcano.repositories;

import java.util.List;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Player;

public interface GameRepository {

	public Game createGame(Player playerOne, Player playerTwo);

	public Game findGameById(Long gameid);

	public List<Game> findAllGames();
	
	public void removeGame(Game game);
		
}
