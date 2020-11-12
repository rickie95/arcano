package com.riccardomalavolti.arcano.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

	private final static Long userOneID = (long) 1;
	private final static Long userTwoID = (long) 2;

	private User p1, p2;
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
		p1 = new User();
		p1.setId(userOneID);
		p1.setUsername(p1Username);
		p2 = new User();
		p2.setId(userTwoID);
		p2.setUsername(p2Username);
		userList = new ArrayList<>(Arrays.asList(p1, p2));
	}

	@Test
	void testGetAlluser() {
		when(userRepo.getAllUsers()).thenReturn(userList);
		List<UserBrief> returnedList = userService.getAllUsers();

		verify(userRepo).getAllUsers();

		assertThat(returnedList).contains(UserMapper.toUserBrief(p1), UserMapper.toUserBrief(p2));
	}

	@Test
	void testGetuserById() {
		when(userRepo.getUserById(userOneID)).thenReturn(Optional.of(p1));

		User returnedPlayer = userService.getUserById(userOneID);

		verify(userRepo).getUserById(userOneID);
		assertEquals(p1, returnedPlayer);
	}

	@Test
	void testGetANonExistentPlayerById() {
		when(userRepo.getUserById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> userService.getUserById((long) (0)));
	}
	
	@Test
	void testGetUserByUsername() {
		User user = new User();
		user.setId((long) (1));
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
		when(userRepo.addNewUser(p1)).thenReturn(p1);

		UserDetails returnedPlayer = userService.addNewUser(p1);
		verify(userRepo).addNewUser(p1);
		verify(userRepo).getUserByUsername(p1Username);

		assertThat(returnedPlayer).isEqualTo(UserMapper.toUserDetails(p1));
	}

	@Test
	void testAddPlayerWhenWithAnAlreadyExistentPlayer() {
		when(userRepo.getUserByUsername(p1.getUsername())).thenReturn(Optional.of(p1));

		assertThatExceptionOfType(ConflictException.class).isThrownBy(() -> {
			userService.addNewUser(p1);
		});
	}

	@Test
	void testDeletePlayer() {
		String owner = "FOO";
		when(userRepo.getUserById(p1.getId())).thenReturn(Optional.of(p1));
		when(userRepo.removeUser(p1)).thenReturn(p1);

		UserDetails returnedPlayer = userService.deleteUser(userOneID, owner);
		verify(userRepo).removeUser(userCaptor.capture());

		verify(authService).verifyOwnershipOf(p1, owner);
		assertEquals(p1, userCaptor.getValue());
		assertThat(returnedPlayer).isEqualTo(UserMapper.toUserDetails(p1));
	}

	@Test
	void testUpdatePlayer() {
		String owner = "FOO";
		User user = new User();
		user.setUsername("MIKE");
		when(userRepo.getUserById(userOneID)).thenReturn(Optional.of(p1));
		when(userRepo.mergeUser(user)).thenReturn(user);

		UserDetails returnedPlayer = userService.updateUser(userOneID, user, owner);

		verify(authService).verifyOwnershipOf(p1, owner);
		verify(userRepo).mergeUser(userCaptor.capture());
		assertThat(returnedPlayer).isEqualTo(UserMapper.toUserDetails(p1));
	}

}
