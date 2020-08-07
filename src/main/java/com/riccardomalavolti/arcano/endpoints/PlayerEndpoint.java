package com.riccardomalavolti.arcano.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.PlayerRepo;

@Path("/player")
public class PlayerEndpoint {
	
	/*
	@Inject
	private PlayerRepo playerRepo;
	*/
	
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> getPlayerList() {
		
		List<Player> playerList = new ArrayList<>();
		playerList.add(new Player(1, "Mike"));
		playerList.add(new Player(2, "Jack"));
		
		return playerList;
	}
	
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Player getPlayer(@PathParam("id") String playerId) {
		return new Player(1, "Mike");
	}


}
