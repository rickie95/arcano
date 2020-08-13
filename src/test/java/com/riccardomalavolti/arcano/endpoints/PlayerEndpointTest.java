package com.riccardomalavolti.arcano.endpoints;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.service.PlayerService;

@ExtendWith(MockitoExtension.class)
class PlayerEndpointTest {
	
	@Mock private PlayerService playerService;
	
	@InjectMocks private PlayerEndpoint playerEndpoint;
	
	@Test
	void getPlayersList() {
		playerEndpoint.getPlayerList();
		verify(playerService).getAllPlayers();
	}

	@Test
	void getPlayerByID() {
		String id = "1";
		playerEndpoint.getPlayer(id);
		verify(playerService).getPlayerById(id);
	}
}
