package com.riccardomalavolti.arcano.repositories;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.persistence.GenericDAO;

@Default
public class PlayerRepository {

	
	final GenericDAO<Player> playerDAO;
	
	@Inject
	public PlayerRepository(GenericDAO<Player> playerDAO) {
		this.playerDAO = playerDAO;
		this.playerDAO.setClass(Player.class);
	}

	public List<Player> getAllPlayers() {
		return playerDAO.findAll();
	}

	public Player getPlayerById(Long playerId) {
		return playerDAO.findById(playerId);
	}

	public Player addPlayer(Player p) {
		return playerDAO.persist(p);
	}

	public void removePlayer(Player player) {
		playerDAO.delete(player);
	}

	public void mergePlayer(Player player) {
		playerDAO.merge(player);		
	}

}
