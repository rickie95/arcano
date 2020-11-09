package com.riccardomalavolti.arcano.persistence;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Match;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MatchDAOTest {
	
	@Mock EntityManager em;
	@Mock TypedQuery<Match> query;
	
	@InjectMocks
	MatchDAO matchDAO;
	
	@Test
	void testGetAllMatches() {
		Match m1 = new Match();
		Long matchOneId = (long) 1;
		m1.setId(matchOneId);
		
		Match m2 = new Match();
		Long matchTwoId = (long) 2;
		m2.setId(matchTwoId);
		
		List<Match> matchList = new ArrayList<Match>(Arrays.asList(m1, m2));
		
		when(em.createQuery(MatchDAO.SELECT_ALL_MATCHES, Match.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(matchList);
		
		List<Match> returnedList = matchDAO.findAll();
		
		assertThat(returnedList).containsExactlyInAnyOrderElementsOf(matchList);
		
	}
	
	@Test
	void testFindMatchOfEvent() {
		Event event = new Event();
		event.setId((long)10);
		
		Match m1 = new Match();
		Long matchOneId = (long) 1;
		m1.setId(matchOneId);
		m1.setParentEvent(event);
		
		Match m2 = new Match();
		Long matchTwoId = (long) 2;
		m2.setId(matchTwoId);
		m1.setParentEvent(event);
		
		List<Match> matchList = new ArrayList<Match>(Arrays.asList(m1, m2));
		
		when(em.createQuery(MatchDAO.MATCHES_OF_EVENT, Match.class)).thenReturn(query);
		when(query.setParameter("eventId", event.getId())).thenReturn(query);
		when(query.getResultList()).thenReturn(matchList);
		
		List<Match> returnedList = matchDAO.findMatchOfEvent(event.getId());
		
		verify(query).setParameter("eventId", event.getId());
		verify(em).createQuery(MatchDAO.MATCHES_OF_EVENT, Match.class);
		
		assertThat(returnedList).containsExactlyInAnyOrderElementsOf(matchList);
	}

}
