package com.riccardomalavolti.arcano.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.persistence.MatchDAO;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MatchRepositoryTest {
	

	@Mock
	private MatchDAO matchDAO;
	
	@Captor ArgumentCaptor<Class<Match>> DAOParameter;
	
	
	@InjectMocks
	private MatchRepository matchRepo;
	
	@Test
	void testDAOClassHasBeingSet(){
		verify(matchDAO).setClass(DAOParameter.capture());
		assertThat(DAOParameter.getValue()).isEqualTo(Match.class);
	}
	
	@Test
	void testGetAllMatches() {
		matchRepo.getAllMatches();
		verify(matchDAO).findAll();
	}
	
	@Test
	void testAddMatch() {
		UUID matchId = UUID.randomUUID(); 
		Match match = new Match(matchId);
		when(matchDAO.persist(match)).thenReturn(match);
		
		Match returnedMatch = matchRepo.addMatch(match);
		
		assertThat(returnedMatch).isEqualTo(match);
	}
	
	@Test
	void testRemoveMatchShouldReturnDeletedMatchIfFound() {
		UUID matchId = UUID.randomUUID(); 
		Match match = new Match(matchId);
		when(matchDAO.delete(match)).thenReturn(match);
		
		Match removedMatch = matchRepo.removeMatch(match);
		
		assertThat(removedMatch).isEqualTo(match);
	}
	
	@Test
	void testRemoveMatchShouldReturnNullIfMatchToBeDeletedIsNotFound() {
		UUID matchId = UUID.randomUUID(); 
		Match match = new Match(matchId);
		when(matchDAO.delete(match)).thenReturn(null);
		
		assertThat(matchRepo.removeMatch(match)).isNull();
	}
	
	@Test
	void testGetMatchByIdShouldReturnTheSearchedMatch() {
		UUID matchId = UUID.randomUUID(); 
		Match match = new Match(matchId);
		
		when(matchDAO.findById(matchId)).thenReturn(match);
		
		Match returnedMatch = matchRepo.getMatchById(matchId).get();
		assertThat(returnedMatch).isEqualTo(match);
	}
	
	@Test
	void testGetMatchByIdShouldReturnNullIfNotFound() {
		UUID matchId = UUID.randomUUID();
		
		when(matchDAO.findById(matchId)).thenReturn(null);
		assertThat(matchRepo.getMatchById(matchId)).isEmpty();
	}
	
	@Test
	void testUpdateMatch() {
		UUID matchId = UUID.randomUUID();
		Match match = new Match(matchId);
		
		when(matchDAO.merge(match)).thenReturn(match);
		
		Match returnedMatch = matchRepo.updateMatch(match);
		assertThat(returnedMatch).isEqualTo(match);
	}
	
	@Test
	void testUpdateMatchIfNotAvailableShouldReturnNull() {
		UUID matchId = UUID.randomUUID();
		Match match = new Match(matchId);
		
		when(matchDAO.merge(match)).thenReturn(null);
		
		assertThat(matchRepo.updateMatch(match)).isNull();
	}
	
	@Test
	void testGetListOfMatchesOfEvent() {
		UUID eventId = UUID.randomUUID();
		Event event = new Event();
		event.setId(eventId);
		Match m1 = new Match();
		m1.setId(UUID.randomUUID());
		m1.setMatchEnded(true);
		m1.setParentEvent(event);
		Match m2 = new Match();
		m2.setId(UUID.randomUUID());
		m2.setMatchEnded(false);
		m2.setParentEvent(event);
		List<Match> matchList = new ArrayList<Match>(Arrays.asList(m1, m2));
		
		when(matchDAO.findMatchOfEvent(eventId)).thenReturn(matchList);	
		
		List<Match> returnedList = matchRepo.getMatchForEvent(eventId);
		
		assertThat(returnedList).containsExactlyElementsOf(matchList);
		for(Match m : returnedList) {
			assertThat(m.getParentEvent()).isEqualTo(event);
		}
		
	}
	
}
