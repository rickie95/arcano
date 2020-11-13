package com.riccardomalavolti.arcano.dto;

import java.util.List;

import com.riccardomalavolti.arcano.model.Game;

public class MatchDetails {
	
	private Long id;
	private EventDetails parentEvent;
	private UserDetails playerOne;
	private UserDetails playerTwo;
	private UserDetails winner;
	
	private short playerOneScore;
	private short playerTwoScore;
	private List<Game> gameList;
	private boolean matchEnded;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public EventDetails getParentEvent() {
		return parentEvent;
	}
	public void setParentEvent(EventDetails parentEvent) {
		this.parentEvent = parentEvent;
	}
	public UserDetails getPlayerOne() {
		return playerOne;
	}
	public void setPlayerOne(UserDetails playerOne) {
		this.playerOne = playerOne;
	}
	public UserDetails getPlayerTwo() {
		return playerTwo;
	}
	public void setPlayerTwo(UserDetails playerTwo) {
		this.playerTwo = playerTwo;
	}
	public UserDetails getWinner() {
		return winner;
	}
	public void setWinner(UserDetails winner) {
		this.winner = winner;
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
	public List<Game> getGameList() {
		return gameList;
	}
	public void setGameList(List<Game> gameList) {
		this.gameList = gameList;
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
		MatchDetails other = (MatchDetails) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}