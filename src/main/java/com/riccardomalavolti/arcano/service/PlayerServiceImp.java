package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.PlayerRepositoryMySQL;

@Default
public class PlayerServiceImp implements PlayerService {
	
	@Inject
	private PlayerRepositoryMySQL playerRepo;

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
		return playerRepo.getAllPlayers();
	}

	@Override
	public Player getPlayerById(Long playerId) {
		return playerRepo.getPlayerById(playerId);
	}

	@Override
	public boolean commitChanges() {
		try {
		playerRepo.commitChanges();
		return true;
		}catch(Exception ex) {
			System.out.println();
		}
		return false;
	}

}
