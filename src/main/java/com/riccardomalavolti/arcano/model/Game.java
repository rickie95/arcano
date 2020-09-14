package com.riccardomalavolti.arcano.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Game {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Player playerOne;
	@ManyToOne
	private Player playerTwo;
	@ManyToOne
	private Player winnerPlayer;
	
	private short playerOneLifePoints;
	private short playerTwoLifePoints;
	
	protected Game() {
	}

	public Game(Long gameId) {
		this.id = gameId;
	}

	public Game(Long gameId, Player playerOne, Player playerTwo) {
		this.id = gameId;
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long gameid) {
		this.id = gameid;
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
	
	public Player getWinnerPlayer() {
		return winnerPlayer;
	}
	
	public void setWinnerPlayer(Player winnerPlayer) {
		this.winnerPlayer = winnerPlayer;
	}
	
	public short getPlayerOneLifePoints() {
		return playerOneLifePoints;
	}
	
	public void setPlayerOneLifePoints(short playerOneLifePoints) {
		this.playerOneLifePoints = playerOneLifePoints;
	}
	
	public short getPlayerTwoLifePoints() {
		return playerTwoLifePoints;
	}
	
	public void setPlayerTwoLifePoints(short playerTwoLifePoints) {
		this.playerTwoLifePoints = playerTwoLifePoints;
	}

	
	
}
