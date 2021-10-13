package com.riccardomalavolti.arcano.endpoints.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;


@ServerEndpoint(value = "/ws-arcano/{playerId}")
public class PlayerWebSocketEndpoint {
    
    private static Map<UUID, Session> playerSessionsMap = new HashMap<UUID, Session>();

    private UUID playerId;

    @OnOpen
    public void onOpen(Session session, @PathParam("playerId") String playerId){
        this.playerId = UUID.fromString(playerId);
        playerSessionsMap.put(this.playerId, session);
    }

    @OnMessage
    public void OnMessage(Session session, String message) throws EncodeException, IOException {

        String recipient = message.split(" ")[0];
        
        playerSessionsMap.get(UUID.fromString(recipient)).getBasicRemote().sendObject(message);

    }

    @OnClose
	public void onClose(Session session) throws IOException {
		playerSessionsMap.remove(this.playerId);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println(session.toString() + " ||| " + throwable.toString());
	}

}
