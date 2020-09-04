package com.riccardomalavolti.arcano.service;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.MatchRepository;

public class MatchService {

	private MatchRepository matchRepo;

	public Match createMatch(Player playerOne, Player playerTwo) {
		Match m = new Match();
		m.setPlayerOne(playerOne);
		m.setPlayerTwo(playerTwo);
		return matchRepo.addMatch(m);
	}

}
