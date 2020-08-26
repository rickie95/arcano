package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.exceptions.ConflictException;
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.PlayerRepository;

@Default
public class PlayerService {
	
	@Inject
	private PlayerRepository playerRepo;

	public List<Player> getAllPlayers() {
		return playerRepo.getAllPlayers();
	}

	public Player getPlayerById(String playerId) {
		return playerRepo
				.getPlayerById(Long.parseLong(playerId))
				.orElseThrow(() -> new NotFoundException("No player exist with id" + playerId));
	}

	public Player addPlayer(Player p) {
		try {
			return playerRepo.addPlayer(p);
		}catch(EntityExistsException ex) {
			throw new ConflictException();
		}
	}

	public Player updatePlayer(String id, Player p) {
		// TODO Auto-generated method stub
		return null;
	}

	public Player deletePlayer(String playerId) {
		Player p = new Player();
		p.setId(Long.parseLong(playerId));
		return playerRepo
				.removePlayer(p)
				.orElseThrow(() -> new NotFoundException("No player exist with id " + playerId));
	}

}
