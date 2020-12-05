package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.dto.UserDetails;
import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.repositories.GameRepository;

public class GameService {
	
	public static final String GAME_NOT_FOUND = "Game with id %s cannot be found";

	@Inject
	private GameRepository gameRepository;
	@Inject
	private UserService userService;

	public UserDetails getWinnerOfGame(Long gameId) {
		return userService
				.getUserDetailsById(
				findGameById(gameId)
				.getWinnerId());
	}

	public Game createGame(Game newGame) {
		return gameRepository.createGame(newGame);
	}
	
	public Game getGameById(Long gameId) {
		return findGameById(gameId);
	}

	private Game findGameById(Long gameid) {
		return gameRepository.findGameById(gameid)
				.orElseThrow(() -> new NotFoundException(String.format(GAME_NOT_FOUND, gameid)));
	}

	public List<Game> allGames() {
		return gameRepository.findAllGames();
	}
	
	public void setPointsForPlayerInGame(Long gameId, Long playerId, Short points) {
		findGameById(gameId).setPointsForPlayer(playerId, points);
	}
	
	public Short getPointForPlayerInGame(Long playerId, Long gameId) {
		return findGameById(gameId).getPointsForPlayer(playerId);
	}

	public Long getOpponentIdForPlayerInGame(Long playerId, Long gameId) {
		return findGameById(gameId).opponentOf(playerId);
	}

	public void withdrawPlayer(Long gameId, Long playerId) {
		Game game = findGameById(gameId);
		game.withdrawPlayer(playerId);
		game.endGame();
	}

}
