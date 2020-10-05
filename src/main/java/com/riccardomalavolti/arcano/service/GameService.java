package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.GameRepository;
import com.riccardomalavolti.arcano.repositories.UserRepository;

public class GameService {
	
	public static final String GAME_NOT_FOUND = "Game with id %s cannot be found";

	@Inject
	private GameRepository gameRepository;
	@Inject
	private UserRepository playerRepository;

	public User getWinnerOfGame(Long gameId) {
		return playerRepository.getPlayerById(getGameById(gameId).getWinnerId())
				.orElseThrow(() -> new NotFoundException("Cannot retrive informations about the winner."));
	}

	public Game createGame(User playerOne, User playerTwo) {
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
