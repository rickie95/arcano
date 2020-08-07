package com.riccardomalavolti.arcano.repositories;

import java.util.List;

import com.riccardomalavolti.arcano.model.Player;

public class PlayerRepo {
	
	private List<Player> playerList;
	
	public PlayerRepo() {
		playerList.add(new Player(1, "Mike"));
		playerList.add(new Player(2, "Jack"));
	}

	public List<Player> getAllPlayers() {
		return playerList;
	}

	public Player getPlayerById(String playerId) {
		return new Player(1, "Mike");		
	}

}
