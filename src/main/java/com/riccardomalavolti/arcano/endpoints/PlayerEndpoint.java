package com.riccardomalavolti.arcano.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.service.PlayerService;


@Path("players")
public class PlayerEndpoint {
	
	
	@Inject
	private PlayerService playerService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> getPlayerList() {
		return playerService.getAllPlayers();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Player getPlayer(@PathParam("id") String id) {
		return playerService.getPlayerById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postNewPlayer(Player p, @Context UriInfo uriInfo) throws URISyntaxException {
		Player saved = playerService.addPlayer(p);
		return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
				.entity(saved)
				.build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePlayer(@PathParam("id") String id, Player updatedPlayer) {
		updatedPlayer.setId(Long.parseLong(id));
		return Response
				.ok(playerService.updatePlayer(updatedPlayer))
				.build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePlayer(@PathParam("id") String id) {
		return Response
			.accepted(playerService.deletePlayer(id))
			.build();
	}


}
