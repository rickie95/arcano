package com.riccardomalavolti.arcano.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.persistence.UserDAO;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class UserRepositoryTest {
	
	private static final Long USER_ID = (long) 1;

	@Mock
	UserDAO userDAO;
	
	@Captor ArgumentCaptor<Class<User>> DAOParameter;
	@Captor ArgumentCaptor<Long> userId;
	@Captor ArgumentCaptor<User> userCaptor;
	
	@InjectMocks
	UserRepository userRepo;
	
	private User user;
	
	@Test
	void testDAOClassHasBeingSet() {
		Mockito.verify(userDAO).setClass(DAOParameter.capture());
		assertEquals(DAOParameter.getValue(), User.class);
	}
	
	@Test
	void testRetrivingAllUsersIfListIsNull() {
		List<User> userList = userRepo.getAllUsers();
		Mockito.verify(userDAO).findAll();
		assertThat(userList).isEmpty();
	}
	
	@Test
	void testRetriveAllUsersIfListIsNotNull() {
		List<User> userCollection = new ArrayList<User>(Arrays.asList(user));
		when(userDAO.findAll()).thenReturn(userCollection);
		
		List<User> userList = userRepo.getAllUsers();
		Mockito.verify(userDAO).findAll();
		assertThat(userList).containsExactlyElementsOf(userCollection);
	}
	
	@Test
	void testSearchingUserById() {
		user = new User();
		user.setId(USER_ID);
		when(userDAO.findById(USER_ID)).thenReturn(user);
		Optional<User> returnedUser = userRepo.getUserById(USER_ID);
		Mockito.verify(userDAO).findById(userId.capture());
		assertThat(returnedUser).isNotEmpty();
		assertThat(returnedUser.get().getId()).isEqualTo(USER_ID);
	}
	
	@Test
	void testGetUserByUsername() {
		String username = "FOO";
		user = new User();
		user.setUsername(username);
		when(userDAO.findUserByUsername(username)).thenReturn(user);
		
		User returnedUser = userRepo.getUserByUsername(username).get();
		assertThat(returnedUser.getUsername()).isEqualTo(username);
	}
	
	@Test
	void testGetUserByUsernameShouldReturnNullIfNotFound() {
		String username = "FOO";
		user = new User();
		user.setUsername(username);
		when(userDAO.findUserByUsername(username)).thenReturn(null);
		
		Optional<User> returnedUser = userRepo.getUserByUsername(username);
		assertThat(returnedUser).isEmpty();
	}
	
	@Test
	void testAddingUser() {
		user = new User();
		when(userDAO.persist(user)).thenReturn(user);
		User returnedUser = userRepo.addNewUser(user);
		Mockito.verify(userDAO).persist(userCaptor.capture());
		assertThat(returnedUser).isNotNull();
		assertEquals(user, userCaptor.getValue());
	}
	
	@Test
	void removePlayer() {
		user = new User();
		user.setId(USER_ID);
		when(userDAO.delete(user)).thenReturn(user);
		
		User returnedPlayer = userRepo.removeUser(user);
		
		verify(userDAO).delete(userCaptor.capture());
		assertEquals(user, userCaptor.getValue());
		assertEquals(user, returnedPlayer);
	}
	
	@Test
	void testMergePlayerShouldReturnTheMergedEntity() {
		user = new User();
		when(userDAO.merge(user)).thenReturn(user);
		User returnedUser = userRepo.mergeUser(user);
		verify(userDAO).merge(userCaptor.capture());
		assertThat(returnedUser).isNotNull();
		assertThat(user).isEqualTo(userCaptor.getValue());
	}
	
	@Test
	void testAuthenticateUser() {
		user = new User();
		user.setId(USER_ID);
		userRepo.authenticate(user);
		verify(userDAO).authenticateUser(userCaptor.capture());
		assertThat(userCaptor.getValue().getId()).isEqualTo(USER_ID);
	}

}
