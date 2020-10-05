package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.MatchRepository;
import com.riccardomalavolti.arcano.repositories.UserRepository;

public class MatchService {

	private static final String NO_MATCH_FOUND_WITH_ID = "No match found with id %s";
	
	@Inject
	private MatchRepository matchRepo;
	
	@Inject
	private UserRepository playerRepo;

	public Match createMatch(Match match) {
		User p1 = playerRepo.getPlayerById(match.getPlayerOne().getId())
		.orElseThrow(() -> new NotFoundException("No player exist with id" + match.getPlayerOne().getId()));
		match.setPlayerOne(p1);
				
		User p2 = playerRepo.getPlayerById(match.getPlayerTwo().getId())
				.orElseThrow(() -> new NotFoundException("No player exist with id" + match.getPlayerTwo().getId()));
		match.setPlayerTwo(p2);
		
		return matchRepo.addMatch(match);
	}

	public List<Match> getAllMatches() {
		return matchRepo.getAllMatches();
	}
	
	public Match getMatchById(String matchId) {
		return matchRepo.getMatchById(Long.parseLong(matchId))
				.orElseThrow(() -> new NotFoundException(String.format(NO_MATCH_FOUND_WITH_ID, matchId)));
	}
	
	public Match deleteMatch(String matchId) {
		Match m = new Match();
		m.setId(Long.parseLong(matchId));
		return matchRepo.removeMatch(m)
				.orElseThrow(() -> new NotFoundException(String.format(NO_MATCH_FOUND_WITH_ID, matchId)));
	}

	public Match updateMatch(String matchId, Match newMatch) {
		newMatch.setId(Long.parseLong(matchId));
		return matchRepo.updateMatch(newMatch)
				.orElseThrow(() -> new NotFoundException(String.format(NO_MATCH_FOUND_WITH_ID, matchId)));
	}
}
