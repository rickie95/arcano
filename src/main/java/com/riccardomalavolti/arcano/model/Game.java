package com.riccardomalavolti.arcano.model;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class Game {
	
	private Long id;

	private boolean isEnded;
	
	private Map<UUID, Short> gamePoints;
	
	public Game() {
		this.isEnded = false;
		gamePoints = new ConcurrentHashMap<>();
	}
	
	public Game(Long id) {
		this();
		this.id = id;
	}
	
	public void withdrawPlayer(UUID playerId) {
		setPointsForPlayer(playerId, (short)0);
		setWinner(opponentOf(playerId));
	}
	
	public void endGame() {
		this.isEnded = true;
	}

	public UUID opponentOf(UUID playerId) {
		return gamePoints.keySet().stream().filter( key -> !key.equals(playerId)).findFirst().get();
	}
	
	public Map<UUID, Short> getGamePoints() {
		return gamePoints;
	}

	public void setGamePoints(Map<UUID, Short> gamePoints) {
		this.gamePoints = gamePoints;
	}

	public void setEnded(boolean isEnded) {
		this.isEnded = isEnded;
	}
	
	public synchronized void setPointsForPlayer(UUID playerId, Short points) {
		gamePoints.put(playerId, points);
	}
	
	public synchronized Short getPointsForPlayer(UUID playerId) {
		return gamePoints.get(playerId);
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public UUID getWinnerId() {
		if(isEnded)
			return Collections.max(gamePoints.entrySet(), Map.Entry.comparingByValue()).getKey();
		
		return null;
	}
	
	public boolean isEnded() {
		return this.isEnded;
	}
	
	private void setWinner(UUID playerId) {
		this.setWinner(playerId);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", isEnded=" + isEnded + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
