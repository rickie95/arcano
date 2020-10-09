package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.exceptions.AccessDeniedException;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Role;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.EventRepository;

public class EventService implements OwnershipVerifier {
	
	private static final String NO_EVENT_FOUND_WITH_ID = "No event found with id %s";

	@Inject EventRepository eventRepo;
	@Inject UserService userService;
	
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

	@Override
	public void isUserOwnerOfResource(String userUsername, Long resourceId) {
		Event event = getEventById(resourceId);
		User requester = userService.getUserByUsername(userUsername);
		if(!event.isOwnedBy(requester))
			throw new AccessDeniedException("You are not the owner of this resource");
		
	}

	public User enrollJudgeInEvent(User judge, Long eventId) {
		if(judge.getRole() != Role.JUDGE)
			throw new NotFoundException("User is not a judge");
		
		return getEventById(eventId).addJudge(judge);
	}

	public List<User> getJudgeList(Long eventId) {
		return getEventById(eventId).getJudgeList();
	}

	public List<User> getPlayersForEvent(Long eventId) {
		return getEventById(eventId).getPlayerList();
	}

}
