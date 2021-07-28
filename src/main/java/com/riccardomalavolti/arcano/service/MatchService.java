package com.riccardomalavolti.arcano.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.dto.MatchBrief;
import com.riccardomalavolti.arcano.dto.MatchDetails;
import com.riccardomalavolti.arcano.dto.MatchMapper;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.MatchRepository;

@Default
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
	
	public List<MatchBrief> getAllMatches() {
		return MatchMapper.toMatchBrief(matchRepo.getAllMatches());
	}
	
	public List<MatchDetails> getAllMatchesDetailed() {
		return MatchMapper.toMatchDetails(matchRepo.getAllMatches());
	}
	
	Match getMatchById(UUID matchId) {
		return matchRepo.getMatchById(matchId)
				.orElseThrow(() -> new NotFoundException(String.format(NO_MATCH_FOUND_WITH_ID, matchId)));
	}	
	
	public MatchDetails getMatchDetailsById(UUID matchId) {
		return MatchMapper.toMatchDetails(getMatchById(matchId));
	}

	public MatchDetails createMatch(Match match) {
		User p1 = userService.getUserById(match.getPlayerOne().getId());
		match.setPlayerOne(p1);
				
		User p2 = userService.getUserById(match.getPlayerTwo().getId());
		match.setPlayerTwo(p2);
		
		return addMatch(match);
	}
	
	public MatchDetails addMatch(Match match) {
		return MatchMapper.toMatchDetails(matchRepo.addMatch(match));
	}
	
	public MatchDetails deleteMatch(UUID matchId, String requesterUsername) {
		Match requestedMatch = getMatchById(matchId);
		authorization.verifyOwnershipOf(requestedMatch, requesterUsername);
		
		return MatchMapper.toMatchDetails(matchRepo.removeMatch(requestedMatch));
	}

	public MatchDetails updateMatch(UUID matchId, Match newMatch, String requesterUsername) {
		Match requestedMatch = getMatchById(matchId);
		authorization.verifyOwnershipOf(requestedMatch, requesterUsername);
		
		newMatch.setId(getMatchById(matchId).getId());
		return MatchMapper.toMatchDetails(matchRepo.updateMatch(newMatch));
	}

	public List<MatchDetails> getMatchListForEvent(UUID eventId) {
		return MatchMapper.toMatchDetails(matchRepo.getMatchForEvent(eventId));
	}

}
