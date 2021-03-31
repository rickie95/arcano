package com.riccardomalavolti.arcano.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
class UserDAOTest {

	private static final Long USER_ONE_ID = (long) 1;
	private static final Long USER_TWO_ID = (long) 2;
	private static final String USER_ONE_USERNAME = "FOO";
	
	private User userOne = new User();
	private User userTwo = new User();
	
	@Mock EntityManager em;
	@Mock TypedQuery<User> query;
	
	@InjectMocks UserDAO userDAO;
	
	
	@Test
	void testFindAllUsers() {
		userOne.setId(USER_ONE_ID);
		userTwo.setId(USER_TWO_ID);
		
		List<User> list = new ArrayList<User>(Arrays.asList(userOne, userTwo));
		
		when(em.createQuery(UserDAO.SELECT_ALL_USERS, User.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(list);
		
		List<User> returnedList = userDAO.findAll();
		
		assertThat(returnedList).contains(userOne, userTwo);
		
	}
	
	@Test
	void testFindByUsernameShouldReturnTheSearchedUser() {
		userOne.setId(USER_ONE_ID);
		userOne.setUsername(USER_ONE_USERNAME);
		
		when(em.createQuery(UserDAO.USER_BY_USERNAME, User.class)).thenReturn(query);
		when(query.setParameter("username", USER_ONE_USERNAME)).thenReturn(query);
		when(query.getSingleResult()).thenReturn(userOne);
		
		User returnedUser = userDAO.findUserByUsername(USER_ONE_USERNAME);
		
		assertThat(returnedUser).isNotNull();
		assertThat(returnedUser.getUsername()).isEqualTo(USER_ONE_USERNAME);
	}
	
	@Test
	void testFindByUsernameShouldThrowNoResultExceptionIfNoUserCanBeFound() {
		
		when(em.createQuery(UserDAO.USER_BY_USERNAME, User.class)).thenReturn(query);
		when(query.setParameter("username", USER_ONE_USERNAME)).thenReturn(query);
		when(query.getSingleResult()).thenThrow(new NoResultException());
		
		User returnedUser = userDAO.findUserByUsername(USER_ONE_USERNAME);
		
		assertThat(returnedUser).isNull();
	}
	
}
