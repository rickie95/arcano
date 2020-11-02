package com.riccardomalavolti.arcano.endpoints.graphql;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.service.AuthenticationService;
import com.riccardomalavolti.arcano.service.EventService;

@GraphQLApi
public class EventGraphQLProvider {
	
	@Inject
	EventService eventService;
	
	@Inject
	AuthenticationService authService;
	
	@Query("eventList")
	public List<Event> getEvents() {
		return eventService.getAllEvents();
	}
	
	@Query("eventById")
	public Event getEventById(@Name("id") Long eventId) {
		return eventService.getEventById(eventId);
	}
	
	@Mutation("addEvent")
	public Event addEvent(Event event) {
		return eventService.createEvent(event);
	}
	
	@Mutation("updateEvent")
	public Event updateEvent(Event event, @Name("jwt") String token) {
		return eventService.updateEvent(event.getId(), event, authService.parseToken(token));
	}
	

}
