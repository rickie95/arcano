package com.riccardomalavolti.arcano.endpoints.rest;

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

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.service.MatchService;

@Path(MatchEndpoint.BASE_PATH)
public class MatchEndpoint {
	
	public static final String BASE_PATH = "matches";

	@Inject private MatchService matchService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Match> getMatches(){
		return matchService.getAllMatches();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Match getMatchById(@PathParam("id") String matchId) {
		return matchService.getMatchById(matchId);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertNewMatch(Match match, @Context UriInfo uriInfo) throws URISyntaxException{
		Match saved = matchService.createMatch(match);
		return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
				.entity(saved)
				.build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMatch(@PathParam("id") String matchId) {
		return Response
				.accepted(matchService.deleteMatch(matchId))
				.build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMatch(@PathParam("id") String matchId, Match updatedMatch) {
		return Response
				.ok(matchService.updateMatch(matchId, updatedMatch))
				.build();
	}
	
}
