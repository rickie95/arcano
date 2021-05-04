package com.riccardomalavolti.arcano.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.persistence.EventDAO;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EventRepositoryTest {

	@Mock
	EventDAO eventDAO;

	@InjectMocks
	EventRepository eventRepo;

	@Test
	void testGetAllEvents() {
		UUID eventID = UUID.randomUUID();
		Event eventOne = new Event(eventID);

		UUID eventTwoId = UUID.randomUUID();
		Event eventTwo = new Event(eventTwoId);

		List<Event> eventList = new ArrayList<Event>(Arrays.asList(eventOne, eventTwo));
		when(eventDAO.findAll()).thenReturn(eventList);

		List<Event> returnedList = eventRepo.getAllEvents();

		assertThat(returnedList).containsExactlyInAnyOrderElementsOf(eventList);
	}

	@Test
	void testGetEventById() {
		UUID eventID = UUID.randomUUID();
		Event eventOne = new Event(eventID);

		when(eventDAO.findById(eventID)).thenReturn(eventOne);

		Optional<Event> returnedEvent = eventRepo.getEventById(eventID);
		assertThat(returnedEvent).isPresent().hasValue(eventOne);
	}

	@Test
	void testGetEventByIdShouldReturnANullableIfNotPresent() {
		UUID eventId = UUID.randomUUID();

		when(eventDAO.findById(eventId)).thenReturn(null);

		Optional<Event> returnedEvent = eventRepo.getEventById(eventId);
		assertThat(returnedEvent).isEmpty();
	}

	@Test
	void testAddEvent() {
		UUID eventID = UUID.randomUUID();
		Event eventOne = new Event(eventID);

		when(eventDAO.persist(eventOne)).thenReturn(eventOne);

		Event returnedEvent = eventRepo.addEvent(eventOne);

		assertThat(returnedEvent).isEqualTo(eventOne);
	}

	@Test
	void testRemoveAnEvent() {
		UUID eventID = UUID.randomUUID();
		Event eventOne = new Event(eventID);

		when(eventDAO.delete(eventOne)).thenReturn(eventOne);

		Event deletedEvent = eventRepo.removeEvent(eventOne);

		assertThat(deletedEvent).isEqualTo(eventOne);
	}

	@Test
	void testRemoveANonExistentEventShouldReturnNull() {
		UUID eventID = UUID.randomUUID();
		Event eventOne = new Event(eventID);

		when(eventDAO.delete(eventOne)).thenReturn(null);

		Event deletedEvent = eventRepo.removeEvent(eventOne);

		assertThat(deletedEvent).isNull();
	}
	
	@Test
	void testUpdateEvent() {
		UUID eventID = UUID.randomUUID();
		Event eventOne = new Event(eventID);

		when(eventDAO.merge(eventOne)).thenReturn(eventOne);

		Event updatedEvent = eventRepo.updateEvent(eventOne);

		assertThat(updatedEvent).isEqualTo(eventOne);
	}
	
	@Test
	void testUpdateANonExistentEventShouldReturnNull() {
		UUID eventID = UUID.randomUUID();
		Event eventOne = new Event(eventID);

		when(eventDAO.merge(eventOne)).thenReturn(null);

		Event updatedEvent = eventRepo.updateEvent(eventOne);

		assertThat(updatedEvent).isNull();
	}

}
