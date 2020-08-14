package com.riccardomalavolti.arcano.endpoints;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.service.PlayerService;


@Path("/player")
public class PlayerEndpoint {
	
	
	@Inject
	private PlayerService playerService;
	
	
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> getPlayerList() {		
		return playerService.getAllPlayers();
	}
	
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Player getPlayer(@PathParam("id") String playerId) {
		return playerService.getPlayerById(playerId);
	}


}
