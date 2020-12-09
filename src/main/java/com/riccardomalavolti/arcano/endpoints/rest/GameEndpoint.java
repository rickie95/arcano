package com.riccardomalavolti.arcano.endpoints.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.service.GameService;

@RequestScoped
@Path(GameEndpoint.BASE_PATH)
public class GameEndpoint {
	
	
		public static final String BASE_PATH = "games";

		@Inject private GameService gameService;
		
		@Context SecurityContext context;
		
		@GET
		@Path("{id}")
		@Produces(MediaType.APPLICATION_JSON)
		@PermitAll
		public Game getGameById(@PathParam("id") Long gameId){
			return gameService.getGameById(gameId);
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@PermitAll
		public Response insertNewGame(Game newGame, @Context UriInfo uriInfo)  throws URISyntaxException {
			Game saved = gameService.createGame(newGame);
			return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
					.entity(saved)
					.build();
		}

}
