package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.repositories.MatchRepository;
import com.riccardomalavolti.arcano.repositories.PlayerRepository;

public class MatchService {

	@Inject
	private MatchRepository matchRepo;
	
	@Inject
	private PlayerRepository playerRepo;

	public Match createMatch(Match match) {
		Player p1 = playerRepo.getPlayerById(
				match.getPlayerOne().getId())
		.orElseThrow(() -> new NotFoundException("No player exist with id" + match.getPlayerOne().getId()));
		System.out.println("REPO" + p1);
		match.setPlayerOne(p1);
				
		Player p2 = playerRepo.getPlayerById(match.getPlayerTwo().getId())
				.orElseThrow(() -> new NotFoundException("No player exist with id" + match.getPlayerTwo().getId()));
		System.out.println("REPO" + p2);
		match.setPlayerTwo(p2);
		
		return matchRepo.addMatch(match);
	}

	public List<Match> getAllMatches() {
		return matchRepo.getAllMatches();
	}
}
