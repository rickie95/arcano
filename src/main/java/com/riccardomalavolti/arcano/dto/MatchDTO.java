package com.riccardomalavolti.arcano.dto;

public class MatchDTO {
	
	Long id;
	UserDTO userOne;
	UserDTO userTwo;
	
	EventDTO parentEvent;
	
	short playerOneScore;
	short playerTwoScore;
	boolean matchEnded;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UserDTO getUserOne() {
		return userOne;
	}
	public void setUserOne(UserDTO userOne) {
		this.userOne = userOne;
	}
	public UserDTO getUserTwo() {
		return userTwo;
	}
	public void setUserTwo(UserDTO userTwo) {
		this.userTwo = userTwo;
	}	
	public EventDTO getParentEvent() {
		return parentEvent;
	}
	public void setParentEvent(EventDTO parentEvent) {
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
