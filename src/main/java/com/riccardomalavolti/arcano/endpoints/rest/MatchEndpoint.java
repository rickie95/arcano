package com.riccardomalavolti.arcano.endpoints.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
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
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.riccardomalavolti.arcano.dto.MatchBrief;
import com.riccardomalavolti.arcano.dto.MatchDetails;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.service.MatchService;

@RequestScoped
@Path(MatchEndpoint.BASE_PATH)
public class MatchEndpoint implements ResourceEndpoint {
	
	public static final String BASE_PATH = "matches";

	@Inject private MatchService matchService;
	
	@Context SecurityContext context;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<MatchBrief> getMatches(@Context UriInfo uriInfo){
		return matchService.getAllMatches().stream()
				.map(match -> match.addUri(getResourceUri(uriInfo.getBaseUri(), match.getId())))
				.collect(Collectors.toList());
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response getMatchById(@PathParam("id") UUID matchId, @Context UriInfo uriInfo) {
		MatchDetails match = matchService.getMatchDetailsById(matchId);
		return Response.ok(match)
			.links(match.getLinks(uriInfo.getBaseUri().toString()).toArray(Link[]::new))
			.build();
	}
	
	@GET
	@Path("ofEvent/{eventId}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<MatchDetails> getMatchListForEvent(@PathParam("eventId") UUID eventId) {
		return  matchService.getMatchListForEvent(eventId);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response insertNewMatch(Match match, @Context UriInfo uriInfo) throws URISyntaxException{
		MatchDetails saved = matchService.createMatch(match);
		return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
				.entity(saved)
				.build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response deleteMatch(@PathParam("id") UUID matchId) {
		return Response
				.accepted(matchService.deleteMatch(matchId, getRequester()))
				.build();
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response updateMatch(@PathParam("id") UUID matchId, Match updatedMatch) {
		return Response
				.ok(matchService.updateMatch(matchId, updatedMatch, getRequester()))
				.build();
	}
	
	private String getRequester() {
		return context.getUserPrincipal().getName();
	}

	@Override
	public String getResourceUri(URI baseUri, UUID resourceId) {
		return baseUri.toString() + BASE_PATH + "/" + resourceId.toString();
	}
	
}
