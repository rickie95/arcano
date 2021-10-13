package com.riccardomalavolti.arcano.endpoints.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.service.GameService;

@ServerEndpoint(value = "/ws-arcano/{game}/{playerId}")
public class GameWebSocketEndpoint {

	/*
	 * Player reaches this endpoint after he gets the url, so the Game is correctly
	 * instanciated before starting.
	 */
	private static HashMap<Long, List<Session>> sessionsMap = new HashMap<>();
	//private static HashMap<String, String> opponents = new HashMap<>();
	
	@Inject
	GameService gameService;

	Long gameId;
	UUID playerId;
	Game currentGame;

	@OnOpen
	public void onOpen(Session session, @PathParam("game") String gameId, @PathParam("playerId") String playerId) throws IOException {
		try {
			this.gameId = Long.parseLong(gameId);
			this.playerId = UUID.fromString(playerId);
			this.currentGame = gameService.getGameById(Long.parseLong(gameId));
		} catch (Exception e) {
			// Game not found probably or problem parsing IDs, 
			// nuke everything and close the session
			session.close();
			return;
		}
		
		// register the session with the gameId as key
		List<Session> sessionList = new ArrayList<Session>();
		if(sessionsMap.containsKey(this.gameId)){
			sessionList = sessionsMap.get(this.gameId);	
		}

		sessionList.add(session);
		sessionsMap.put(this.gameId, sessionList);
	}

	@OnMessage
	public void updatePoints(Session session, Short points) throws IOException, EncodeException {
		// update game
		gameService.setPointsForPlayerInGame(this.gameId, this.playerId, points);
		
		// send notification at the opponent
		for(Session s : sessionsMap.get(this.gameId)){
			if(s.isOpen() && s != session){
				s.getBasicRemote().sendObject(points);
			}
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		sessionsMap.get(this.gameId).remove(session);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println(session.toString() + " ||| " + throwable.toString());
	}

}
