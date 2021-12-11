package com.riccardomalavolti.arcano.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity(name=Match.ENTITY_NAME)
public class Match implements Ownable {
	
	public static final String ENTITY_NAME = "Incontro";

	@Id
	@Type(type="uuid-char")
	@GeneratedValue(strategy = GenerationType.AUTO)
	UUID id;
	
	@ManyToOne
	@JoinColumn(name = "event_id", referencedColumnName = "id")
	Event parentEvent;

	@ManyToOne
    @JoinColumn(name = "playerone_id", referencedColumnName = "id")	
	User playerOne;
	
    @ManyToOne
    @JoinColumn(name = "playertwo_id", referencedColumnName = "id")
	User playerTwo;

	@Transient List<Game> gameList;
	
	short playerOneScore;
	short playerTwoScore;

	@Transient User winner;
	@Transient boolean matchEnded;
	
	
	
	public Match() {
		
	}

	public Match(UUID matchId) {
		this();
		this.id = matchId;
	}

	public boolean isMatchEnded() {
		return matchEnded;
	}

	public void setMatchEnded(boolean matchEnded) {
		this.matchEnded = matchEnded;
	}

	public User getWinner() {
		winner = null;
		
		//updateMatchStatus();
		
		if (playerOneScore > playerTwoScore) {
			winner = playerOne;
		}else if(playerOneScore < playerTwoScore){
			winner = playerTwo;
		}
		return winner;
	}

	private void updateMatchStatus() {
		playerOneScore = 0;
		playerTwoScore = 0;
		
		if(gameList == null)
			return;
		
		for(Game g : gameList)
			if(g.isEnded()) {
				if(g.getWinnerId().equals(playerOne.getId())) {
					playerOneScore++;
				}else if(g.getWinnerId().equals(playerTwo.getId())) {
					playerTwoScore++;
				}
			}
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID matchid) {
		this.id = matchid;
	}
	
	public User getPlayerOne() {
		return playerOne;
	}
	
	public void setPlayerOne(User playerOne) {
		this.playerOne = playerOne;
	}
	
	public User getPlayerTwo() {
		return playerTwo;
	}
	
	public void setPlayerTwo(User playerTwo) {
		this.playerTwo = playerTwo;
	}
	
	public short getPlayerOneScore() {
		return playerOneScore;
	}
	
	public void setPlayerOneScore(int playerOneScore) {
		this.playerOneScore = (short) playerOneScore;
	}
	
	public short getPlayerTwoScore() {
		return playerTwoScore;
	}
	
	public void setPlayerTwoScore(int i) {
		this.playerTwoScore = (short) i;
	}
	
	public Event getParentEvent() {
		return parentEvent;
	}

	public void setParentEvent(Event parentEvent) {
		this.parentEvent = parentEvent;
	}

	@Override
	public boolean isOwnedBy(User user) {
		return user.equals(playerOne) || user.equals(playerTwo) || parentEvent.isOwnedBy(user);
	}
	
	@Override
	public String toString() {
		return "Match [id=" + id + ", playerOne=" + playerOne + ", playerTwo=" + playerTwo + ", playerOneScore="
				+ playerOneScore + ", playerTwoScore=" + playerTwoScore + "]";
	}

	public List<Game> getGameList() {
		return gameList;
	}

	public void setGameList(List<Game> gameList) {
		if(gameList == null)
			gameList = new ArrayList<>();
		
		this.gameList = gameList;
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
		Match other = (Match) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
