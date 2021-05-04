package com.riccardomalavolti.arcano.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Link;

import com.riccardomalavolti.arcano.model.Game;

public class MatchDetails implements RESTResource {
	
	private UUID id;
	private EventDetails parentEvent;
	private UserDetails playerOne;
	private UserDetails playerTwo;
	private UserDetails winner;
	
	private short playerOneScore;
	private short playerTwoScore;
	private List<Game> gameList;
	private boolean matchEnded;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
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
	public List<Link> getLinks(String absoluteBasePath) {
		List<Link> links = new ArrayList<Link>();
		// SELF
		links.add(Link.fromUri("{base_uri}/matches/{id}")
			.rel("self").type("text/plain")
			.build(absoluteBasePath, this.id));

		// PlayerOne
		links.add(Link.fromUri("{base_uri}/users/{id}")
			.rel("playerOne").type("text/plain")
			.build(absoluteBasePath, playerOne.getId()));
		
		// PlayerTwo
		links.add(Link.fromUri("{base_uri}/users/{id}")
			.rel("playerTwo").type("text/plain")
			.build(absoluteBasePath, playerTwo.getId()));
		
		// ParentEvent
		links.add(Link.fromUri("{base_uri}/events/{id}")
			.rel("event").type("text/plain")
			.build(absoluteBasePath, parentEvent.getId()));

		// Games
		for(Game g: gameList){
			links.add(Link.fromUri("{base_uri}/games/{id}")
			.rel(String.format("game_{}", g.getId())).type("text/plain")
			.build(absoluteBasePath, g.getId()));
		}
		return links;
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
