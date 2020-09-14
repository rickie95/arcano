package com.riccardomalavolti.arcano.graphql;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.service.MatchService;

@GraphQLApi
public class MatchGraphQLProvider {
	
	@Inject MatchService matchService;
	
	@Query
	public List<Match> getMatches(){
		return matchService.getAllMatches();
	}
	
	@Mutation
	public Match insertMatch(Match match) {
		return matchService.createMatch(match);
	}

}