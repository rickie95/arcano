package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.EventRepository;

public class EventService {
	
	private static final String NO_EVENT_FOUND_WITH_ID = "No event found with id %s";

	@Inject EventRepository eventRepo;
	@Inject UserService userService;
	
	@Inject AuthorizationService authService;
	
	public List<Event> getAllEvents() {
		return eventRepo.getAllEvents();
	}

	public Event getEventById(Long eventId) {
		return eventRepo.getEventById(eventId)
				.orElseThrow(() -> new NotFoundException(String.format(NO_EVENT_FOUND_WITH_ID, eventId)));
	}

	public User enrollPlayerInEvent(User player, Long eventId) {
		return getEventById(eventId).enrollPlayer(player);
	}

	public Event createEvent(Event event) {
		return eventRepo.addEvent(event);
	}

	public Event removeEvent(Long eventId) {
		return eventRepo.removeEvent(getEventById(eventId));
	}
	
	public Event updateEvent(Long eventId, Event event, String requesterUsername) {
		Event requestedEvent = getEventById(eventId);
		authService.verifyOwnershipOf(requestedEvent, requesterUsername);
		event.setId(requestedEvent.getId());
		
		return eventRepo.updateEvent(event);
	}

	public User enrollJudgeInEvent(User judge, Long eventId, String requesterUsername) {
		Event requestedEvent = getEventById(eventId);
		authService.verifyOwnershipOf(requestedEvent, requesterUsername);
		
		return requestedEvent.addJudge(judge);
	}

	public List<User> getJudgeList(Long eventId) {
		return getEventById(eventId).getJudgeList();
	}

	public List<User> getPlayersForEvent(Long eventId) {
		return getEventById(eventId).getPlayerList();
	}

}
