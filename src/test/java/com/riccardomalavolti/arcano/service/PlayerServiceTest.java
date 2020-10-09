package com.riccardomalavolti.arcano.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.ws.rs.NotFoundException;

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

import com.riccardomalavolti.arcano.exceptions.ConflictException;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class PlayerServiceTest {
	
	private final static String p1Username = "Mike";
	private final static String p2Username = "Joe";
	
	private final static Long playerOneID = (long) 1;
	private final static Long playerTwoID = (long) 2;
	
	private User p1, p2;
	List<User> playerList;
	
	@Captor
	ArgumentCaptor<User> playerCaptor;
	
	@Mock
	UserRepository playerRepo;
	
	@InjectMocks
	UserService playerService;
	
	@BeforeEach
	void setupRepository() {
		p1 = new User();
		p1.setId(playerOneID); p1.setUsername(p1Username);
		p2 = new User();
		p2.setId(playerTwoID); p2.setUsername(p2Username);
		playerList = new ArrayList<>(Arrays.asList(p1, p2));
	}
	
	@Test
	void testGetAllPlayer() {		
		when(playerRepo.getAllPlayers()).thenReturn(playerList);
		List<User> returnedList = playerService.getAllUsers();
		
		verify(playerRepo).getAllPlayers();
		
		assertEquals(playerList, returnedList);
	}
	
	@Test
	void testGetPlayerById() {
		when(playerRepo.getUserById(playerOneID)).thenReturn(Optional.of(p1));
		
		User returnedPlayer = playerService.getUserById(playerOneID);
		
		verify(playerRepo).getUserById(playerOneID);
		assertEquals(p1, returnedPlayer);
	}
	
	@Test
	void testGetANonExistentPlayerById() {
		when(playerRepo.getUserById(anyLong()))
			.thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> playerService.getUserById((long)(0)));
	}
	
	@Test
	void testAddPlayer() {
		when(playerRepo.addNewUser(p1)).thenReturn(p1);
		
		User returnedPlayer = playerRepo.addNewUser(p1);
		verify(playerRepo).addNewUser(p1);
		
		assertEquals(p1, returnedPlayer);
	}
	
	@Test
	void testAddPlayerWhenWithAnAlreadyExistentPlayer() {
		when(playerRepo.addNewUser(p1)).thenThrow(EntityExistsException.class);

		assertThrows(ConflictException.class, () -> playerService.addNewUser(p1));
	}
	
	@Test
	void testDeletePlayer() {
		when(playerRepo.getUserById(p1.getId())).thenReturn(Optional.of(p1));
		when(playerRepo.removeUser(p1)).thenReturn(p1);
		
		User returnedPlayer = playerService.deleteUser(playerOneID);
		verify(playerRepo).removeUser(playerCaptor.capture());
		
		assertEquals(p1, playerCaptor.getValue());
		assertEquals(returnedPlayer, p1);
	}
	
	@Test
	void testDeletePlayerIfNotPresentShouldThrowsANotFoundException() {
		assertThrows(NotFoundException.class, () -> playerService.deleteUser(playerOneID));
	}
	
	@Test
	void testUpdatePlayer() {
		when(playerRepo.getUserById(p1.getId())).thenReturn(Optional.of(p1));
		when(playerRepo.mergeUser(p1)).thenReturn(p1);
		
		User returnedPlayer = playerService.updateUser(p1.getId(), p1);
		
		verify(playerRepo).mergeUser(p1);
		assertEquals(returnedPlayer, p1);
		
	}

}
