package com.riccardomalavolti.arcano.endpoints.graphql;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.riccardomalavolti.arcano.dto.EventBrief;
import com.riccardomalavolti.arcano.dto.EventDetails;
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
	public List<EventBrief> getEvents() {
		return eventService.getAllEvents();
	}
	
	@Query("eventById")
	public EventDetails getEventById(@Name("id") Long eventId) {
		return eventService.getEventById(eventId);
	}
	
	@Mutation("addEvent")
	public EventDetails addEvent(Event event) {
		return eventService.createEvent(event);
	}
	
	@Mutation("updateEvent")
	public EventDetails updateEvent(Event event, @Name("jwt") String token) {
		return eventService.updateEvent(event.getId(), event, authService.parseToken(token));
	}
	

}
