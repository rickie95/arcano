package com.riccardomalavolti.arcano.endpoints.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.riccardomalavolti.arcano.service.GameService;

@ServerEndpoint(value = "/ws-arcano/{game}/{player}")
public class WebSocketEndpoint {

	/*
	 * Player reaches this endpoint after he gets the url, so the Game is correctly
	 * instanciated before starting.
	 */
	private static HashMap<String, GameSession> sessions = new HashMap<>();
	private static HashMap<String, String> opponents = new HashMap<>();
	
	@Inject
	GameService gameService;

	private Session session;
	private GameSession gameSession;
	
	private class GameSession {

		private Long gameId;
		private UUID playerId;
		private WebSocketEndpoint endpoint;

		public GameSession(String gameId, String playerId, WebSocketEndpoint endpoint) {
			this.gameId = Long.parseLong(gameId);
			this.playerId = UUID.fromString(playerId);
			this.endpoint = endpoint;
		}
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("game") String gameId, @PathParam("player") String playerId) {
		// Player registers itself, associating his name to the opponent in the hash map
		this.session = session;
		this.gameSession = new GameSession(gameId, playerId, this);
		sessions.put(session.getId(), this.gameSession);
		opponents.put(playerId, gameService.getOpponentIdForPlayerInGame(gameSession.playerId, gameSession.gameId).toString());

	}

	@OnMessage
	public void updatePoints(Session session, Short points) throws IOException, EncodeException {
		// update game
		gameService.setPointsForPlayerInGame(gameSession.gameId, gameSession.playerId, points);
		
		// send notification at the opponent
		String opponentId = opponents.get(gameSession.playerId.toString());
		sendMessage(sessions.get(opponentId).endpoint, points);
	}
	
	public void message(Session session, boolean hasResign) throws IOException, EncodeException {
		gameService.withdrawPlayer(gameSession.gameId, gameSession.playerId);
		
		String opponentId = opponents.get(gameSession.playerId.toString());
		sendMessage(sessions.get(opponentId).endpoint, hasResign);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		sessions.remove(session.getId());
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
	}

	private static synchronized void sendMessage(WebSocketEndpoint endpoint, Object message) 
			throws IOException, EncodeException {
		try {
			endpoint.session.getBasicRemote().sendObject(message);
		} catch (IOException | EncodeException e) {
			e.printStackTrace();
		}

	}

}
