package com.riccardomalavolti.arcano.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity(name=Match.ENTITY_NAME)
public class Match {
	
	public static final String ENTITY_NAME = "Incontro";

	@Id
	@GeneratedValue
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "event_id", referencedColumnName = "id")
	Event parentEvent;

	@ManyToOne
    @JoinColumn(name = "playerone_id", referencedColumnName = "id")	
	User playerOne;
	
    @ManyToOne
    @JoinColumn(name = "playertwo_id", referencedColumnName = "id")
	User playerTwo;
	
	short playerOneScore;
	short playerTwoScore;

	@Transient User winner;
	@Transient List<Game> gameList;
	@Transient boolean matchEnded;
	
	
	@Override
	public String toString() {
		return "Match [id=" + id + ", playerOne=" + playerOne + ", playerTwo=" + playerTwo + ", playerOneScore="
				+ playerOneScore + ", playerTwoScore=" + playerTwoScore + "]";
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

	public User getWinner() {
		if (playerOneScore > playerTwoScore) {
			winner = playerOne;
		}else {
			winner = playerTwo;
		}
		return winner;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long matchid) {
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
	
}
