package com.riccardomalavolti.arcano.endpoints.graphql;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.PlayerService;

@GraphQLApi
public class PlayerGraphQLProvider{
	
	@Inject PlayerService playerService;
	
	@Query
	public List<User> getPlayers(){
		return playerService.getAllPlayers();
	}
	
	@Query
	public User getPlayerById(@Name("id") String playerId) {
		return playerService.getPlayerById(playerId);
	}
	
	@Query("passwordList")
	public List<String> getUsersPasswordList(@Source List<User> players){
		return players.stream().map(User::getUsername).collect(Collectors.toList());
	}
	
	@Mutation
	public User createPlayer(User player) {
		return playerService.addPlayer(player);
	}

}
