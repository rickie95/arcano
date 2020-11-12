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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchBrief other = (MatchBrief) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
