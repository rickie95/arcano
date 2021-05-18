package com.riccardomalavolti.arcano.endpoints.graphql;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.riccardomalavolti.arcano.dto.MatchDetails;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.service.AuthenticationService;
import com.riccardomalavolti.arcano.service.MatchService;

@GraphQLApi
public class MatchGraphQLProvider {
	
	@Inject MatchService matchService;
	
	@Inject AuthenticationService authService;
	
	@Query
	public List<MatchDetails> getMatches(){
		return matchService.getAllMatchesDetailed();
	}
	
	@Query
	public MatchDetails getMatchById(@Name("matchId") UUID matchId) {
		return matchService.getMatchDetailsById(matchId);
	}
	
	@Mutation
	public MatchDetails insertMatch(Match match) {
		return matchService.createMatch(match);
	}
	
	@Mutation
	public MatchDetails removeMatch(@Name("matchId") UUID matchId, @Name("jwt") String token) {
		return matchService.deleteMatch(matchId, authService.parseToken(token));
	}
	
	@Mutation
	public MatchDetails updateMatch(@Name("matchId") UUID matchId, Match updatedMatch, @Name("jwt") String token) {
		return matchService.updateMatch(matchId, updatedMatch, authService.parseToken(token));
	}

}
