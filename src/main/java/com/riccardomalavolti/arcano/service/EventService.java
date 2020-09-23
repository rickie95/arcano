package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.EventRepository;

public class EventService {
	
	private static final String NO_EVENT_FOUND_WITH_ID = "No event found with id %s";

	@Inject EventRepository eventRepo;
	
	public List<Event> getAllEvents() {
		return eventRepo.getAllEvents();
	}

	public Event getEventById(Long id) {
		return eventRepo.getEventById(id)
				.orElseThrow(() -> new NotFoundException(String.format(NO_EVENT_FOUND_WITH_ID, id)));
	}

	public Player enrollPlayerInEvent(Player player, Long eventId) {
		return null;
	}

	public Event createEvent(Event event) {
		return null;
	}

}
