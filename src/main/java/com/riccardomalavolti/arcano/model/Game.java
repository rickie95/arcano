package com.riccardomalavolti.arcano.model;


public class Game {
	
	private Long id;
	
	private Player playerOne;
	private Player playerTwo;
	private Player winnerPlayer;
	
	private short playerOneLifePoints;
	private short playerTwoLifePoints;
	
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
