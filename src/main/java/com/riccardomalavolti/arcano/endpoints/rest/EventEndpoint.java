package com.riccardomalavolti.arcano.endpoints.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event getEventById(@PathParam("id") String eventId) {
		return eventService.getEventById(Long.parseLong(eventId));
	}
	
	@POST
	@Path("{id}/enroll")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response enrollPlayerInEvent(@PathParam("id") String eventId, User player) {
		return Response
				.accepted(eventService.enrollPlayerInEvent(player, Long.parseLong(eventId)))
				.build();
	}

}
