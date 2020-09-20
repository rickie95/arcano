package com.riccardomalavolti.arcano.persistence;

import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Event;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EventDAOTest {
	
	@Mock EntityManager em;
	@Mock TypedQuery<Event> query;
	
	@InjectMocks
	EventDAO eventDAO;
	
	@Test
	void testGetEventList() {
		Event eOne = new Event();
		eOne.setId((long)(1));
		Event eTwo = new Event();
		eTwo.setId((long)(2));
		
		List<Event> eventList = new ArrayList<Event>(Arrays.asList(eOne, eTwo));
		
		when(em.createQuery(EventDAO.SELECT_ALL_EVENTS, Event.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(eventList);
		
		List<Event> returnedList = eventDAO.findAll();
		
		assertThat(returnedList).containsExactlyInAnyOrderElementsOf(eventList);
	}

}
