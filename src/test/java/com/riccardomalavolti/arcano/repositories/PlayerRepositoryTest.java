package com.riccardomalavolti.arcano.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.persistence.MySQLGenericDAO;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class PlayerRepositoryTest {
	
	private static final Long USER_ID = (long) 1;

	@Mock
	MySQLGenericDAO<Player> playerDAO;
	
	@Captor ArgumentCaptor<Class<Player>> DAOParameter;
	@Captor ArgumentCaptor<Long> playerId;
	@Captor ArgumentCaptor<Player> playerCaptor;
	
	@InjectMocks
	PlayerRepository playerRepo;
	
	@Test
	void testDAOClassIsBeingSet() {
		Mockito.verify(playerDAO).setClass(DAOParameter.capture());
		assertEquals(DAOParameter.getValue(), Player.class);
	}
	
	@Test
	void testRetrivingAllPlayers() {
		playerRepo.getAllPlayers();
		Mockito.verify(playerDAO).findAll();
	}
	
	@Test
	void testSearchingPlayerById() {
		Long id = (long) 1;
		
		playerRepo.getPlayerById(id);
		Mockito.verify(playerDAO).findById(playerId.capture());
		assertEquals(playerId.getValue(), id);
	}
	
	@Test
	void testAddingPlayer() {
		Player player = new Player();
		
		playerRepo.addPlayer(player);
		Mockito.verify(playerDAO).persist(playerCaptor.capture());
		assertEquals(player, playerCaptor.getValue());
	}
	
	@Test
	void testAddingPlayerWhenAlreadyExistentShouldThrowEntityExistException() {
		Player player = new Player();
		
		playerRepo.addPlayer(player);
		Mockito.verify(playerDAO).persist(playerCaptor.capture());
		assertEquals(player, playerCaptor.getValue());
	}
	
	@Test
	void removePlayer() {
		Player player = new Player();
		player.setId(USER_ID);
		when(playerDAO.delete(player)).thenReturn(player);
		
		Player returnedPlayer = playerRepo.removePlayer(player).get();
		
		verify(playerDAO).delete(playerCaptor.capture());
		assertEquals(player, playerCaptor.getValue());
		assertEquals(player, returnedPlayer);
	}
	
	@Test
	void testMergePlayerShouldReturnTheMergedEntity() {
		Player player = new Player();
		playerRepo.mergePlayer(player);
		verify(playerDAO).merge(playerCaptor.capture());
		assertEquals(player, playerCaptor.getValue());
	}

}
