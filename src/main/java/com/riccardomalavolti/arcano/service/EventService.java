package com.riccardomalavolti.arcano.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.dto.EventBrief;
import com.riccardomalavolti.arcano.dto.EventDetails;
import com.riccardomalavolti.arcano.dto.EventMapper;
import com.riccardomalavolti.arcano.dto.UserBrief;
import com.riccardomalavolti.arcano.dto.UserDetails;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Role;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.EventRepository;

@Default
public class EventService {
	
	private static final String NO_EVENT_FOUND_WITH_ID = "No event found with id %s";

	@Inject EventRepository eventRepo;
	@Inject UserService userService;
	
	@Inject AuthorizationService authService;
	
	public EventService() {};
	
	public EventService(EventRepository eventRepository, UserService userService, AuthorizationService authService) {
		this();
		this.eventRepo = eventRepository;
		this.userService = userService;
		this.authService = authService;
	}
	
	public List<EventBrief> getAllEvents() {
		return eventRepo.getAllEvents().stream()
				.map(EventMapper::toEventBrief).collect(Collectors.toList());
	}
	
	private Event retriveEventById(Long eventId) {
		return eventRepo.getEventById(eventId)
		.orElseThrow(() -> new NotFoundException(String.format(NO_EVENT_FOUND_WITH_ID, eventId)));
	}

	public EventDetails getEventById(Long eventId) {
		return EventMapper.toEventDetails(retriveEventById(eventId));
	}

	public EventDetails createEvent(Event event) {
		return EventMapper.toEventDetails(eventRepo.addEvent(event));
	}

	public EventDetails removeEvent(Long eventId, String requesterUsername) {
		Event requestedEvent = retriveEventById(eventId);
		authService.verifyOwnershipOf(requestedEvent, requesterUsername);
		return EventMapper.toEventDetails(eventRepo.removeEvent(requestedEvent));
	}
	
	public EventDetails updateEvent(Long eventId, Event event, String requesterUsername) {
		Event requestedEvent = retriveEventById(eventId);
		authService.verifyOwnershipOf(requestedEvent, requesterUsername);
		event.setId(requestedEvent.getId());
		
		return EventMapper.toEventDetails(eventRepo.updateEvent(event));
	}
	
	public EventDetails enrollPlayerInEvent(Long playerId, Long eventId) {
		Event requestedEvent = retriveEventById(eventId);
		User player = userService.getUserById(playerId);
		requestedEvent.enrollPlayer(player);
		return EventMapper.toEventDetails(requestedEvent);
	}

	public UserDetails enrollJudgeInEvent(Long judgeId, Long eventId, String requesterUsername) {
		Event requestedEvent = retriveEventById(eventId);
		authService.verifyOwnershipOf(requestedEvent, requesterUsername);
		User judge = userService.getUserWithRoleById(judgeId, Role.JUDGE);
		requestedEvent.addJudge(judge);
		return UserMapper.toUserDetails(judge);
	}

	public List<UserBrief> getJudgeList(Long eventId) {
		return retriveEventById(eventId).getJudgeList()
				.stream().map(UserMapper::toUserBrief).collect(Collectors.toList());
	}

	public List<UserBrief> getPlayersForEvent(Long eventId) {
		return retriveEventById(eventId).getPlayerList()
				.stream().map(UserMapper::toUserBrief).collect(Collectors.toList());
	}

}
