package com.riccardomalavolti.arcano.repositories;

import java.util.List;

import com.riccardomalavolti.arcano.model.Player;

public interface PlayerRepository {
	
	public List<Player> getAllPlayers();
	public Player getPlayerById(Long playerId);
	public void addPlayer(Player p);
	public boolean commitChanges();

}
