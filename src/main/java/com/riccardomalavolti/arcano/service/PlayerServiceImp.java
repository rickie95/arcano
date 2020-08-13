package com.riccardomalavolti.arcano.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Default;

import com.riccardomalavolti.arcano.model.Player;

@Default
public class PlayerServiceImp implements PlayerService {

	@Override
	public void addPlayer(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePlayer(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePlayer(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Player> getAllPlayers() {

		List<Player> playerList = new ArrayList<>();
		playerList.add(new Player(1, "Mike"));
		playerList.add(new Player(2, "Jack"));
		
		return playerList;
	}

	@Override
	public Player getPlayerById(String id) {
		return new Player(1, "Mike");
	}

}
