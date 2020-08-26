package com.riccardomalavolti.arcano.repositories;

import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.persistence.GenericDAO;
import com.riccardomalavolti.arcano.persistence.MySQLGenericDAO;

@Default
public class PlayerRepository {

	final GenericDAO<Player> playerDAO;
	
	@Inject
	public PlayerRepository(MySQLGenericDAO<Player> playerDAO) {
		this.playerDAO = playerDAO;
		this.playerDAO.setClass(Player.class);
	}

	public List<Player> getAllPlayers() {
		return playerDAO.findAll();
	}

	public Optional<Player> getPlayerById(Long playerId) {
		return Optional.ofNullable(playerDAO.findById(playerId));
	}

	public Player addPlayer(Player p) {
		return playerDAO.persist(p);
	}

	public Optional<Player> removePlayer(Player player) {
		return Optional.ofNullable(playerDAO.delete(player));
	}

	public void mergePlayer(Player player) {
		playerDAO.merge(player);		
	}

}
