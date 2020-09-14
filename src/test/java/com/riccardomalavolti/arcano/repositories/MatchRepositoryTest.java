package com.riccardomalavolti.arcano.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.persistence.MySQLGenericDAO;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MatchRepositoryTest {

	@Mock
	private MySQLGenericDAO<Match> matchDAO;
	
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
		Match match = new Match();
		match.setId((long)(1));
		when(matchDAO.persist(match)).thenReturn(match);
		
		Match returnedMatch = matchRepo.addMatch(match);
		
		assertThat(returnedMatch).isEqualTo(match);
	}
	
}
