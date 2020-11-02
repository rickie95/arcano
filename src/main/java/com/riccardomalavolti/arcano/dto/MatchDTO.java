package com.riccardomalavolti.arcano.dto;

public class MatchDTO {
	
	Long id;
	Long playerOneId;
	Long playerTwoId;
	Long parentEvent;
	
	short playerOneScore;
	short playerTwoScore;
	boolean matchEnded;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPlayerOneId() {
		return playerOneId;
	}
	public void setPlayerOneId(Long playerOneId) {
		this.playerOneId = playerOneId;
	}
	public Long getPlayerTwoId() {
		return playerTwoId;
	}
	public void setPlayerTwoId(Long playerTwoId) {
		this.playerTwoId = playerTwoId;
	}
	public Long getParentEvent() {
		return parentEvent;
	}
	public void setParentEvent(Long parentEvent) {
		this.parentEvent = parentEvent;
	}
	public short getPlayerOneScore() {
		return playerOneScore;
	}
	public void setPlayerOneScore(short playerOneScore) {
		this.playerOneScore = playerOneScore;
	}
	public short getPlayerTwoScore() {
		return playerTwoScore;
	}
	public void setPlayerTwoScore(short playerTwoScore) {
		this.playerTwoScore = playerTwoScore;
	}
	public boolean isMatchEnded() {
		return matchEnded;
	}
	public void setMatchEnded(boolean matchEnded) {
		this.matchEnded = matchEnded;
	}

}
