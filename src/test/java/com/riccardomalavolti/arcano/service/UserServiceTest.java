package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.SecurityContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.dto.UserBrief;
import com.riccardomalavolti.arcano.dto.UserDetails;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.exceptions.ConflictException;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class UserServiceTest {

	private final static String p1Username = "Mike";
	private final static String p2Username = "Joe";

	private final static UUID userOneID = UUID.randomUUID();
	private final static UUID userTwoID = UUID.randomUUID();

	private User userOne, userTwo;
	private List<User> userList;

	@Captor
	ArgumentCaptor<User> userCaptor;

	@Mock
	UserRepository userRepo;
	@Mock
	AuthorizationService authService;
	@Mock
	SecurityContext context;

	@InjectMocks
	UserService userService;

	@BeforeEach
	void setupRepository() {
		userOne = new User();
		userOne.setId(userOneID);
		userOne.setUsername(p1Username);
		userOne.setPassword("Foo");
		userTwo = new User();
		userTwo.setId(userTwoID);
		userTwo.setUsername(p2Username);
		userTwo.setPassword("Bar");
		userList = new ArrayList<>(Arrays.asList(userOne, userTwo));
	}

	@Test
	void testGetAlluser() {
		when(userRepo.getAllUsers()).thenReturn(userList);
		List<UserBrief> returnedList = userService.getAllUsers();

		verify(userRepo).getAllUsers();

		assertThat(returnedList).contains(UserMapper.toUserBrief(userOne), UserMapper.toUserBrief(userTwo));
	}

	@Test
	void testGetUserById() {
		when(userRepo.getUserById(userOneID)).thenReturn(Optional.of(userOne));

		User returnedPlayer = userService.getUserById(userOneID);

		verify(userRepo).getUserById(userOneID);
		assertEquals(userOne, returnedPlayer);
	}

	@Test
	void testGetANonExistentPlayerById() {
		UUID userID = UUID.randomUUID();
		
		when(userRepo.getUserById(userID)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> userService.getUserById(userID));
	}
	
	@Test
	void testGetUserByUsername() {
		User user = new User();
		user.setId(UUID.randomUUID());
		user.setUsername("FOO");
		when(userRepo.getUserByUsername("FOO")).thenReturn(Optional.of(user));
		
		User returned = userService.getUserByUsername("FOO");
		assertThat(returned).isEqualTo(user);
	}

	@Test
	void testGetAnUserWithUnknownUsernameShouldThrowNotFoundException() {
		when(userRepo.getUserByUsername("FOO")).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> userService.getUserByUsername("FOO"));
	}

	@Test
	void testAddPlayer() {
		when(userRepo.getUserByUsername(p1Username)).thenReturn(Optional.empty());
		when(userRepo.addNewUser(userOne)).thenReturn(userOne);

		UserDetails returnedPlayer = userService.addNewUser(userOne);
		verify(userRepo).addNewUser(userOne);
		verify(userRepo).getUserByUsername(p1Username);

		assertThat(returnedPlayer).isEqualTo(UserMapper.toUserDetails(userOne));
	}

	@Test
	void testAddPlayerWhenWithAnAlreadyExistentPlayer() {
		when(userRepo.getUserByUsername(userOne.getUsername())).thenReturn(Optional.of(userOne));

		assertThatExceptionOfType(ConflictException.class).isThrownBy(() -> {
			userService.addNewUser(userOne);
		});
	}
	
	@Test
	void testAddPlayerShouldThrowBadRequestExIfUserNameOrPasswordAreNull() {
		// user == null
		assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> {
			userService.addNewUser(null);
		});
		
		// user.username == null
		User nullUsername = new User();
		assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> {
			userService.addNewUser(nullUsername);
		});
		
		// user.username == ""
		User emptyUsername = new User();
		emptyUsername.setUsername("");
		assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> {
			userService.addNewUser(emptyUsername);
		});
		
		// user.username ok, user.password == null
		User nullPassword = new User();
		nullPassword.setUsername("Mike");
		assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> {
			userService.addNewUser(nullPassword);
		});
		
		// user.username ok, user.password == ""
		User emptyPassword = new User();
		emptyPassword.setUsername("Mike");
		emptyPassword.setPassword("");
		assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> {
			userService.addNewUser(emptyPassword);
		});
	}

	@Test
	void testDeletePlayer() {
		String owner = "FOO";
		when(userRepo.getUserById(userOne.getId())).thenReturn(Optional.of(userOne));
		when(userRepo.removeUser(userOne)).thenReturn(userOne);

		UserDetails returnedPlayer = userService.deleteUser(userOneID, owner);
		verify(userRepo).removeUser(userCaptor.capture());

		verify(authService).verifyOwnershipOf(userOne, owner);
		assertEquals(userOne, userCaptor.getValue());
		assertThat(returnedPlayer).isEqualTo(UserMapper.toUserDetails(userOne));
	}

	@Test
	void testUpdatePlayer() {
		String owner = "FOO";
		User user = new User();
		user.setUsername("MIKE");
		user.setPassword("Bar");
		when(userRepo.getUserById(userOneID)).thenReturn(Optional.of(userOne));
		when(userRepo.mergeUser(user)).thenReturn(user);

		UserDetails returnedPlayer = userService.updateUser(userOneID, user, owner);

		verify(authService).verifyOwnershipOf(userOne, owner);
		verify(userRepo).mergeUser(userCaptor.capture());
		assertThat(returnedPlayer).isEqualTo(UserMapper.toUserDetails(userOne));
	}
	
	@Test
	void testUpdatePlayerWhenPasswordIsNotSpecified() {
		String owner = "FOO";
		User user = new User();
		user.setUsername("MIKE");
		when(userRepo.getUserById(userOneID)).thenReturn(Optional.of(userOne));
		when(userRepo.mergeUser(user)).thenReturn(user);

		UserDetails returnedPlayer = userService.updateUser(userOneID, user, owner);

		verify(authService).verifyOwnershipOf(userOne, owner);
		verify(userRepo).mergeUser(userCaptor.capture());
		assertThat(returnedPlayer).isEqualTo(UserMapper.toUserDetails(userOne));
	}
	
	@Test
	void testGetUserDetailsByUsername() {
		when(userRepo.getUserByUsername(p1Username)).thenReturn(Optional.of(userOne));
		
		UserDetails userFetched = userService.getUserDetailsByUsername(p1Username);
		
		assertThat(userFetched.getId()).isEqualTo(userOne.getId());
		
	}
	
	@Test
	void testGetUserDetailsById() {
		when(userRepo.getUserById(userOne.getId())).thenReturn(Optional.of(userOne));
		
		UserDetails userFetched = userService.getUserDetailsById(userOne.getId());
		
		assertThat(userFetched.getId()).isEqualTo(userOne.getId());	
	}
	
}
