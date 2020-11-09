package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.MatchRepository;

public class MatchService {

	public static final String NO_MATCH_FOUND_WITH_ID = "No match found with id %s";
	
	@Inject
	private MatchRepository matchRepo;
	
	@Inject
	private UserService userService;
	
	@Inject 
	private AuthorizationService authorization;
	
	public MatchService() {
		
	}
	
	public MatchService(MatchRepository matchRepository, UserService userService, AuthorizationService authService) {
		this();
		this.matchRepo = matchRepository;
		this.userService = userService;
		this.authorization = authService;
	}
	
	public List<Match> getAllMatches() {
		return matchRepo.getAllMatches();
	}
	
	public Match getMatchById(Long matchId) {
		return matchRepo.getMatchById(matchId)
				.orElseThrow(() -> new NotFoundException(String.format(NO_MATCH_FOUND_WITH_ID, matchId)));
	}	

	public Match createMatch(Match match) {
		User p1 = userService.getUserById(match.getPlayerOne().getId());
		match.setPlayerOne(p1);
				
		User p2 = userService.getUserById(match.getPlayerTwo().getId());
		match.setPlayerTwo(p2);
		
		return matchRepo.addMatch(match);
	}
	
	public Match addMatch(Match match) {
		return matchRepo.addMatch(match);
	}
	
	public Match deleteMatch(Long matchId, String requesterUsername) {
		Match requestedMatch = getMatchById(matchId);
		authorization.verifyOwnershipOf(requestedMatch, requesterUsername);
		
		return matchRepo.removeMatch(requestedMatch);
	}

	public Match updateMatch(Long matchId, Match newMatch, String requesterUsername) {
		Match requestedMatch = getMatchById(matchId);
		authorization.verifyOwnershipOf(requestedMatch, requesterUsername);
		
		newMatch.setId(getMatchById(matchId).getId());
		return matchRepo.updateMatch(newMatch);
	}

	public List<Match> getMatchListForEvent(Long eventId, boolean fetchOnlyInProgress) {
		return matchRepo.getMatchForEvent(eventId, fetchOnlyInProgress);
	}
}
