package com.riccardomalavolti.arcano.repositories;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.persistence.MatchDAO;

public class MatchRepository {
	
	final MatchDAO matchDAO;
	
	@Inject
	public MatchRepository(MatchDAO matchDAO) {
		this.matchDAO = matchDAO;
		this.matchDAO.setClass(Match.class);
	}
	
	public List<Match> getAllMatches(){
		return matchDAO.findAll();
	}

	public Match addMatch(Match match) {
		return matchDAO.persist(match);
	}

	public Optional<Match> removeMatch(Match match) {
		return Optional.ofNullable(matchDAO.delete(match));
	}

	public Optional<Match> getMatchById(Long matchId) {
		return Optional.ofNullable(matchDAO.findById(matchId));
	}

}
