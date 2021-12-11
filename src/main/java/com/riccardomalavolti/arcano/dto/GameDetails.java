package com.riccardomalavolti.arcano.dto;

import java.util.Map;
import java.util.UUID;

public class GameDetails {
	
	private Long id;
	private boolean isEnded;
	private Map<UUID, Short> gamePoints;
	private MatchBrief parentMatch;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isEnded() {
		return isEnded;
	}
	public void setEnded(boolean isEnded) {
		this.isEnded = isEnded;
	}
	public Map<UUID, Short> getGamePoints() {
		return gamePoints;
	}
	public void setGamePoints(Map<UUID, Short> gamePoints) {
		this.gamePoints = gamePoints;
	}
	public MatchBrief getParentMatch() {
		return parentMatch;
	}
	public void setParentMatch(MatchBrief parentMatch) {
		this.parentMatch = parentMatch;
	}

	
}
