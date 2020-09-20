package com.riccardomalavolti.arcano.repositories;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.persistence.EventDAO;

public class EventRepository {

	@Inject
	private EventDAO eventDAO;

	public List<Event> getAllEvents() {
		return eventDAO.findAll();
	}

	public Optional<Event> getEventById(Long eventId) {
		return Optional.ofNullable(eventDAO.findById(eventId));
	}

	public Event addEvent(Event eventOne) {
		return eventDAO.persist(eventOne);
	}

	public Optional<Event> removeEvent(Event eventOne) {
		return Optional.ofNullable(eventDAO.delete(eventOne));
	}

	public Optional<Event> updateEvent(Event eventOne) {
		return Optional.ofNullable(eventDAO.merge(eventOne));
	}

}
