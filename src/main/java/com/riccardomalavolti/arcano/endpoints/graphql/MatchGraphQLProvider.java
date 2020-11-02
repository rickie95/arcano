package com.riccardomalavolti.arcano.endpoints.graphql;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.service.AuthenticationService;
import com.riccardomalavolti.arcano.service.MatchService;

@GraphQLApi
public class MatchGraphQLProvider {
	
	@Inject MatchService matchService;
	
	@Inject AuthenticationService authService;
	
	@Query
	public List<Match> getMatches(){
		return matchService.getAllMatches();
	}
	
	@Query
	public Match getMatchById(@Name("matchId") Long matchId) {
		return matchService.getMatchById(matchId);
	}
	
	@Mutation
	public Match insertMatch(Match match) {
		return matchService.createMatch(match);
	}
	
	@Mutation
	public Match removeMatch(@Name("matchId") Long matchId, @Name("jwt") String token) {
		return matchService.deleteMatch(matchId, authService.parseToken(token));
	}
	
	@Mutation
	public Match updateMatch(@Name("matchId") Long matchId, Match updatedMatch, @Name("jwt") String token) {
		return matchService.updateMatch(matchId, updatedMatch, authService.parseToken(token));
	}

}
