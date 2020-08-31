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
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.PlayerRepository;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class PlayerServiceTest {
	
	private final static String p1Username = "Mike";
	private final static String p2Username = "Joe";
	
	private final static Long p1Id = (long) 1;
	private final static Long p2Id = (long) 2;
	
	private Player p1, p2;
	List<Player> playerList;
	
	@Captor
	ArgumentCaptor<Player> playerCaptor;
	
	@Mock
	PlayerRepository playerRepo;
	
	@InjectMocks
	PlayerService playerService;
	
	@BeforeEach
	void setupRepository() {
		p1 = new Player();
		p1.setId(p1Id); p1.setUsername(p1Username);
		p2 = new Player();
		p2.setId(p2Id); p2.setUsername(p2Username);
		playerList = new ArrayList<>(Arrays.asList(p1, p2));
	}
	
	@Test
	void testGetAllPlayer() {		
		when(playerRepo.getAllPlayers()).thenReturn(playerList);
		List<Player> returnedList = playerService.getAllPlayers();
		
		verify(playerRepo).getAllPlayers();
		
		assertEquals(playerList, returnedList);
	}
	
	@Test
	void testGetPlayerById() {
		when(playerRepo.getPlayerById(p1Id)).thenReturn(Optional.of(p1));
		
		Player returnedPlayer = playerService.getPlayerById(p1Id.toString());
		
		verify(playerRepo).getPlayerById(p1Id);
		assertEquals(p1, returnedPlayer);
	}
	
	@Test
	void testGetANonExistentPlayerById() {
		when(playerRepo.getPlayerById(anyLong()))
			.thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, 
				() -> playerService.getPlayerById("0"));
	}
	
	@Test
	void testAddPlayer() {
		when(playerRepo.addPlayer(p1)).thenReturn(p1);
		
		Player returnedPlayer = playerRepo.addPlayer(p1);
		verify(playerRepo).addPlayer(p1);
		
		assertEquals(p1, returnedPlayer);
	}
	
	@Test
	void testAddPlayerWhenWithAnAlreadyExistentPlayer() {
		when(playerRepo.addPlayer(p1)).thenThrow(EntityExistsException.class);

		
		assertThrows(ConflictException.class, () -> 
			playerService.addPlayer(p1));
	}
	
	@Test
	void testDeletePlayer() {
		String p1IDString = p1Id.toString();
		when(playerRepo.removePlayer(p1)).thenReturn(Optional.of(p1));
		
		Player returnedPlayer = playerService.deletePlayer(p1IDString);
		verify(playerRepo).removePlayer(playerCaptor.capture());
		
		assertEquals(p1, playerCaptor.getValue());
		assertEquals(returnedPlayer, p1);
	}
	
	@Test
	void testDeletePlayerIfNotPresentShouldThrowsANotFoundException() {
		when(playerRepo.removePlayer(p1)).thenThrow(NotFoundException.class);
		String p1IDString = p1Id.toString();
		assertThrows(NotFoundException.class, () -> playerService.deletePlayer(p1IDString));
	}
	
	@Test
	void testUpdatePlayer() {
		when(playerRepo.mergePlayer(p1)).thenReturn(Optional.of(p1));
		
		Player returnedPlayer = playerService.updatePlayer(p1);
		
		verify(playerRepo).mergePlayer(p1);
		assertEquals(returnedPlayer, p1);
		
	}

}
