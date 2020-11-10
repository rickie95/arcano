package com.riccardomalavolti.arcano.dto;

public class MatchBrief {
	
	Long id;
	UserBrief userOne;
	UserBrief userTwo;
	
	EventBrief parentEvent;
	
	short playerOneScore;
	short playerTwoScore;
	boolean matchEnded;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UserBrief getUserOne() {
		return userOne;
	}
	public void setUserOne(UserBrief userOne) {
		this.userOne = userOne;
	}
	public UserBrief getUserTwo() {
		return userTwo;
	}
	public void setUserTwo(UserBrief userTwo) {
		this.userTwo = userTwo;
	}	
	public EventBrief getParentEvent() {
		return parentEvent;
	}
	public void setParentEvent(EventBrief parentEvent) {
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
