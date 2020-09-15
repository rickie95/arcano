package com.riccardomalavolti.arcano.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.service.MatchService;

@Path("matches")
public class MatchEndpoint {

	@Inject private MatchService matchService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Match> getMatches(){
		return matchService.getAllMatches();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertNewMatch(Match match, @Context UriInfo uriInfo) throws URISyntaxException{
		Match saved = matchService.createMatch(match);
		return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
				.entity(saved)
				.build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Match getMatchById(@PathParam("id") String matchId) {
		return matchService.getMatchById(matchId);
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMatch(@PathParam("id") String matchId) {
		return Response
				.accepted(matchService.deleteMatch(matchId))
				.build();
	}
}
