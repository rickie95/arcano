package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.GameRepository;
import com.riccardomalavolti.arcano.repositories.PlayerRepository;

public class GameService {
	
	public static final String GAME_NOT_FOUND = "Game with id %s cannot be found";

	@Inject
	private GameRepository gameRepository;
	@Inject
	private PlayerRepository playerRepository;

	public Player getWinnerOfGame(Long gameId) {
		return playerRepository.getPlayerById(getGameById(gameId).getWinnerId())
				.orElseThrow(() -> new NotFoundException("Cannot retrive informations about the winner."));
	}

	public Game createGame(Player playerOne, Player playerTwo) {
		return gameRepository.createGame(playerOne, playerTwo);
	}

	public Game getGameById(Long gameid) {
		return gameRepository.findGameById(gameid)
				.orElseThrow(() -> new NotFoundException(String.format(GAME_NOT_FOUND, gameid)));
	}

	public List<Game> allGames() {
		return gameRepository.findAllGames();
	}
	
	public void setPointsForPlayerInGame(Long gameId, Long playerId, Short points) {
		getGameById(gameId).setPointsForPlayer(playerId, points);
	}
	
	public Short getPointForPlayerInGame(Long playerId, Long gameId) {
		return getGameById(gameId).getPointsForPlayer(playerId);
	}

	public Long getOpponentIdForPlayerInGame(Long playerId, Long gameId) {
		return getGameById(gameId).opponentOf(playerId);
	}

}
