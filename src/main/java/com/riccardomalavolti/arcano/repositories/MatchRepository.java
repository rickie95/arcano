package com.riccardomalavolti.arcano.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	public Match removeMatch(Match match) {
		return matchDAO.delete(match);
	}

	public Optional<Match> getMatchById(Long matchId) {
		return Optional.ofNullable(matchDAO.findById(matchId));
	}

	public Match updateMatch(Match match) {
		return matchDAO.merge(match);
	}

	public List<Match> getMatchForEvent(Long eventId, boolean fetchOnlyInProgress) {
		List<Match> matchList =  matchDAO.findMatchOfEvent(eventId);
		
		if(fetchOnlyInProgress)
			matchList = matchList.stream()
					.filter(Match::isMatchEnded)
					.collect(Collectors.toList());
		
		return matchList;
	}

}
