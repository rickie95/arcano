package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.GameRepository;

public class GameService {

	@Inject
	private GameRepository gameRepository;

	public Player getWinnerOfGame(Long gameid) {
		Game g = gameRepository.findGameById(gameid);
		return g.getWinnerPlayer();
	}

	public Game createGame(Player playerOne, Player playerTwo) {
		return gameRepository.createGame(playerOne, playerTwo);
	}

	public Game getGameById(Long gameid) {
		return gameRepository.findGameById(gameid);
	}

	public List<Game> allGames() {
		return gameRepository.findAllGames();
	}

}
