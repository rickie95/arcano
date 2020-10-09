package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.exceptions.AccessDeniedException;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.MatchRepository;

public class MatchService implements OwnershipVerifier {

	public static final String NO_MATCH_FOUND_WITH_ID = "No match found with id %s";
	
	@Inject
	private MatchRepository matchRepo;
	
	@Inject
	private UserService userService;
	
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
	
	public Match deleteMatch(Long matchId) {
		Match m = getMatchById(matchId);
		return matchRepo.removeMatch(m);
	}

	public Match updateMatch(Long matchId, Match newMatch) {
		newMatch.setId(getMatchById(matchId).getId());
		return matchRepo.updateMatch(newMatch);
	}

	@Override
	public void isUserOwnerOfResource(String userUsername, Long resourceId) {
		Match match = getMatchById(resourceId);
		User requester = userService.getUserByUsername(userUsername);
		if(!match.getParentEvent().isOwnedBy(requester))
			throw new AccessDeniedException("You are not the owner of this resource");
	}
}
