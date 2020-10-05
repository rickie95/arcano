package com.riccardomalavolti.arcano.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

import com.riccardomalavolti.arcano.model.User;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class PlayerDAOTest {

	
	@Mock EntityManager em;
	@Mock TypedQuery<User> query;
	
	@InjectMocks UserDAO playerDAO;
	
	@Test
	void testFindAllPlayers() {
		User p1 = new User();
		Long playerOneId = (long) 1;
		p1.setId(playerOneId);
		
		User p2 = new User();
		Long playerTwoId = (long) 2;
		p2.setId(playerTwoId);
		
		List<User> list = new ArrayList<User>(Arrays.asList(p1, p2));
		
		when(em.createQuery(UserDAO.SELECT_ALL_PLAYERS, User.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(list);
		
		List<User> returnedList = playerDAO.findAll();
		
		assertThat(returnedList).contains(p1, p2);
		
	}
}
