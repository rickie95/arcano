package com.riccardomalavolti.arcano.endpoints.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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

import com.riccardomalavolti.arcano.dto.EventBrief;
import com.riccardomalavolti.arcano.dto.EventDetails;
import com.riccardomalavolti.arcano.dto.UserBrief;
import com.riccardomalavolti.arcano.dto.UserDetails;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Role;
import com.riccardomalavolti.arcano.service.EventService;

@PermitAll
@Path(EventEndpoint.BASE_PATH)
public class EventEndpoint {

	public static final String BASE_PATH = "events";
	
	@Inject EventService eventService;
	@Context SecurityContext context;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<EventBrief> getAllEvents(){
		return eventService.getAllEvents();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response createNewEvent(Event event, @Context UriInfo uriInfo) throws URISyntaxException {
		EventDetails saved = eventService.createEvent(event);
		return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
				.entity(saved)
				.build();
	}
	
	@DELETE
	@Path("{eventId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Role.Values.ADMIN_VALUE)
	public Response removeEvent(@PathParam("eventId") Long eventId) {		
		return Response.accepted(eventService.removeEvent(eventId, context.getUserPrincipal().getName())).build();
	}

	@GET
	@Path("{eventId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEventById(@PathParam("eventId") Long eventId, @Context UriInfo uriInfo) {
		EventDetails event = eventService.getEventById(eventId);
		return Response.ok(event)
			.links(event.getLinks(uriInfo.getBaseUri().toString()).toArray(Link[]::new))
			.build();
	}
	
	@GET
	@Path("{eventId}/players")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<UserBrief> getPlayersForEvent(@PathParam("eventId") Long eventId) {
		return eventService.getPlayersForEvent(eventId);
	}
	
	
	@PUT
	@Path("{eventId}/players/{playerId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response enrollPlayerInEvent(@PathParam("eventId") Long eventId, @PathParam("playerId") Long playerId) {
		return Response
				.accepted(eventService.enrollPlayerInEvent(playerId, eventId))
				.build();
	}
	
	@DELETE
	@Path("{eventId}/players/{playerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removePlayerInEvent(@PathParam("eventId") Long eventId, @PathParam("playerId") Long playerId) {
		return Response
				.accepted(eventService.removePlayerFromEvent(playerId, eventId, context.getUserPrincipal().getName()))
				.build();
	}
	
	@GET
	@Path("{eventId}/judges")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<UserBrief> getJudgeList(@PathParam("eventId") Long eventId) {
		return eventService.getJudgeList(eventId);
	}
	
	@PUT
	@Path("{eventId}/judges/{judgeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Role.Values.ADMIN_VALUE)
	public Response enrollJudgeInEvent(@PathParam("eventId") Long eventId, @PathParam("judgeId") Long judgeId,
			@Context UriInfo uriInfo) {
		UserDetails judge = eventService.enrollJudgeInEvent(judgeId, eventId, 
		context.getUserPrincipal().getName());
		return Response.accepted(judge)
			.link("event", uriInfo.getBaseUri() + String.format("/events/{}", eventId))
			.build();
	}

}
