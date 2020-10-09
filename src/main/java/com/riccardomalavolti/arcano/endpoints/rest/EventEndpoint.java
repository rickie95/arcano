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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Role;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.EventService;

@PermitAll
@Path(EventEndpoint.BASE_PATH)
public class EventEndpoint {

	public static final String BASE_PATH = "events";
	
	@Inject EventService eventService;
	@Context SecurityContext context;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllEvents(){
		return eventService.getAllEvents();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Role.Values.ADMIN_VALUE)
	public Response createNewEvent(Event event, @Context UriInfo uriInfo) throws URISyntaxException {
		Event saved = eventService.createEvent(event);
		return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
				.entity(saved)
				.build();
	}
	
	@DELETE
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Role.Values.ADMIN_VALUE)
	public Response removeEvent(@PathParam("id") String eventId) {
		
		verifyOwnershipOf(eventId);
		
		return Response.accepted(eventService.removeEvent(Long.parseLong(eventId))).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event getEventById(@PathParam("id") String eventId) {
		return eventService.getEventById(Long.parseLong(eventId));
	}
	
	@GET
	@Path("{id}/players")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<User> getPlayersForEvent(@PathParam("id") String eventId) {
		return eventService.getPlayersForEvent(Long.parseLong(eventId));
	}
	
	
	@PUT
	@Path("{id}/players")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response enrollPlayerInEvent(@PathParam("id") String eventId, User player) {
		return Response
				.accepted(eventService.enrollPlayerInEvent(player, Long.parseLong(eventId)))
				.build();
	}
	
	@GET
	@Path("{id}/judges")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<User> getJudgeList(@PathParam("id") String eventId) {
		return eventService.getJudgeList(Long.parseLong(eventId));
	}
	
	@PUT
	@Path("{id}/judges")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Role.Values.ADMIN_VALUE)
	public Response enrollJudgeInEvent(@PathParam("id") String eventId, User judge) {
		verifyOwnershipOf(eventId);
		return Response.accepted(eventService.enrollJudgeInEvent(judge, Long.parseLong(eventId))).build();
	}
	
	private void verifyOwnershipOf(String eventId) {
		String requesterUsername = context.getUserPrincipal().getName();
		eventService.isUserOwnerOfResource(requesterUsername, Long.parseLong(eventId));
	}

}
