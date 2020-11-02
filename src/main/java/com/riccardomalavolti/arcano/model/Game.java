package com.riccardomalavolti.arcano.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Game {
	
	private Long id;

	private boolean isEnded;
	
	private Map<Long, Short> gamePoints;
	
	public Game(Long id) {
		this.id = id;
		this.isEnded = false;
		gamePoints = new ConcurrentHashMap<>();
	}
	
	public synchronized void setPointsForPlayer(Long playerId, Short points) {
		gamePoints.put(playerId, points);
	}
	
	public synchronized Short getPointsForPlayer(Long playerId) {
		return gamePoints.get(playerId);
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public Long getWinnerId() {
		return Collections.max(gamePoints.entrySet(), Map.Entry.comparingByValue()).getKey();
	}
	
	public boolean isEnded() {
		return this.isEnded;
	}
	
	public void endGame() {
		this.isEnded = true;
	}

	public Long opponentOf(Long playerId) {
		return gamePoints.keySet().stream().filter( key -> !key.equals(playerId)).findFirst().get();
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
	
	private void setWinner(Long playerId) {
		setPointsForPlayer(playerId, (short)20);
	}

	public void withdrawPlayer(Long playerId) {
		setPointsForPlayer(playerId, (short)0);
		setWinner(opponentOf(playerId));
	}
}
