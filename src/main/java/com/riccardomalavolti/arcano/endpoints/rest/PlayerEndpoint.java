package com.riccardomalavolti.arcano.endpoints.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
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

import com.riccardomalavolti.arcano.model.Role;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.PlayerService;

@RequestScoped
@Path("players")
public class PlayerEndpoint {
	
	
	@Inject
	private PlayerService playerService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<User> getPlayerList() {
		return playerService.getAllPlayers();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getPlayer(@PathParam("id") String id) {
		return playerService.getPlayerById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response postNewPlayer(User p, @Context UriInfo uriInfo) throws URISyntaxException {
		User saved = playerService.addPlayer(p);
		return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
				.entity(saved)
				.build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePlayer(@PathParam("id") String id, User updatedPlayer) {
		return Response
				.ok(playerService.updatePlayer(id, updatedPlayer))
				.build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Role.Values.ADMIN_VALUE)
	public Response deletePlayer(@PathParam("id") String id) {
		return Response
			.accepted(playerService.deletePlayer(id))
			.build();
	}


}
