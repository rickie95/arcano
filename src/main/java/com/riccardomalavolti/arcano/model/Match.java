package com.riccardomalavolti.arcano.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Match {

	@Id
	@GeneratedValue
	Long id;
	
	Player playerOne;
	Player playerTwo;
	short playerOneScore;
	short playerTwoScore;

	@Transient Player winner;
	@Transient List<Game> gameList;
	@Transient boolean matchEnded;
	
	
	public Player getWinner() {
		if (playerOneScore > playerTwoScore)
			winner = playerOne;
		
		winner = playerTwo;
		
		return winner;
	}
	
	public Long getId() {
		return id;
	}
	
	public Player getPlayerOne() {
		return playerOne;
	}
	
	public void setPlayerOne(Player playerOne) {
		this.playerOne = playerOne;
	}
	
	public Player getPlayerTwo() {
		return playerTwo;
	}
	
	public void setPlayerTwo(Player playerTwo) {
		this.playerTwo = playerTwo;
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
	
}
