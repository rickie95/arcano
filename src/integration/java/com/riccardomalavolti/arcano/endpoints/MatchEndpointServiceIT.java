package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.riccardomalavolti.arcano.endpoints.rest.MatchEndpoint;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.MatchRepository;
import com.riccardomalavolti.arcano.service.AuthorizationService;
import com.riccardomalavolti.arcano.service.MatchService;
import com.riccardomalavolti.arcano.service.UserService;

import io.restassured.RestAssured;

public class MatchEndpointServiceIT extends JerseyTest {

	MatchService matchService;
	
	@Mock MatchRepository matchRepo;
	@Mock AuthorizationService authService;
	@Mock UserService userService;

	@SuppressWarnings("deprecation")
	@Override
	protected Application configure(){
        MockitoAnnotations.initMocks(this);
        matchService = new MatchService(matchRepo, userService, authService);
        forceSet(TestProperties.CONTAINER_PORT, "0");
        return new ResourceConfig(MatchEndpoint.class)
            .register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(matchService).to(MatchService.class);
                }
            });
    }
	
	@Before
	public void configureRestAssured() {
		RestAssured.baseURI = getBaseUri().toString();
	}
	
	@Test
    public void getMatchList() {
        Match match1 = new Match();
        match1.setId((long)(1));

        Match match2 = new Match();
        match2.setId((long)(2));

        List<Match> matchList = new ArrayList<>(Arrays.asList(match1, match2));
        
        when(matchRepo.getAllMatches()).thenReturn(matchList);

        given()
            .accept(MediaType.APPLICATION_JSON).
        when()
            .get(MatchEndpoint.BASE_PATH).
        then()
            .statusCode(200)
            .assertThat()
            .body(
                "[0].id", equalTo(match1.getId().intValue()),
                "[1].id", equalTo(match2.getId().intValue())
            );
    }
	
	@Test
    public void testGetMatchShouldReturnTheDesiredMatch() {
		Long matchId = (long) 1;
    	Match match1 = new Match();
        match1.setId(matchId);
        
        when(matchRepo.getMatchById(matchId)).thenReturn(Optional.of(match1));
        
        given()
        	.accept(MediaType.APPLICATION_JSON).
        when()
        	.get(MatchEndpoint.BASE_PATH + "/" + matchId.toString()).
        then()
        	.statusCode(200)
        	.assertThat()
        	.body(
        			"id", equalTo(matchId.intValue())
        			);
    }
	
	@Test
    public void testAddMatchShouldReturnTheURLofTheNewMatch() {
    	User p1 = new User(); 
    	p1.setId((long)(1));
    	User p2 = new User(); 
    	p2.setId((long)(2));
    	
    	Match toBeReturnedMatch = new Match((long)(3));
    	toBeReturnedMatch.setPlayerOneScore(1);
    	toBeReturnedMatch.setPlayerTwoScore(2);
    	toBeReturnedMatch.setPlayerOne(p1);
    	toBeReturnedMatch.setPlayerTwo(p2);
    	
    	when(matchRepo.addMatch(any(Match.class))).thenReturn(toBeReturnedMatch);
    	when(userService.getUserById(p1.getId())).thenReturn(p1);
    	when(userService.getUserById(p2.getId())).thenReturn(p2);
    	
    	 JsonObject newMatchJSON = Json.createObjectBuilder()
 				.add("playerOneScore", 1)
 				.add("playerTwoScore", 2)
 				.add("playerOne", Json.createObjectBuilder().add("id", 1).build())
 				.add("playerTwo", Json.createObjectBuilder().add("id", 2).build())
 				.build();
    	
    	given()
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(newMatchJSON.toString()).
    	when()
    		.post(MatchEndpoint.BASE_PATH).
    	then()
    		.statusCode(201)
    		.assertThat()
    			.body(
    					"id", equalTo(3),
    					"playerOneScore", equalTo(1),
    					"playerTwoScore", equalTo(2),
    					"playerOne.id", equalTo(1),
    					"playerTwo.id", equalTo(2)
    					)
    			.header("Location", response -> endsWith(MatchEndpoint.BASE_PATH + "/" + toBeReturnedMatch.getId().toString()));
    }
}