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
}
