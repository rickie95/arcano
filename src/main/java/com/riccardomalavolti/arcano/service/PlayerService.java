package com.riccardomalavolti.arcano.service;

import java.util.List;

import com.riccardomalavolti.arcano.model.Player;

public interface PlayerService {

	public void addPlayer(Player p);
	public void removePlayer(Player p);;
	public void updatePlayer(Player p);
	
	public List<Player> getAllPlayers();
	public Player getPlayerById(String id);
	
}
