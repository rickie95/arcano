package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.riccardomalavolti.arcano.model.Player;
import com.riccardomalavolti.arcano.service.MatchService;

import io.restassured.RestAssured;


public class MatchEnpointTest extends JerseyTest {

    private final Long matchId = (long) 1;

    @Mock private MatchService matchService;

    @SuppressWarnings("deprecation")
	@Override
    protected Application configure(){
        MockitoAnnotations.initMocks(this);
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
        
        when(matchService.getAllMatches()).thenReturn(matchList);

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
    	Match match1 = new Match();
        match1.setId(matchId);
        
        when(matchService.getMatchById(matchId.toString())).thenReturn(match1);
        
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
    public void testDeleteMatchShouldReturnTheEntityOnlyOnce() {
    	Match match = new Match();
        match.setId(matchId);
    	when(matchService.deleteMatch(matchId.toString()))
    		.thenReturn(match)
    		.thenReturn(null);
    	

        given()
        	.accept(MediaType.APPLICATION_JSON).
        when()
        	.delete(MatchEndpoint.BASE_PATH + "/" + matchId.toString()).
        then()
        	.statusCode(202)
        	.assertThat()
        	.body(
        			"id", equalTo(matchId.intValue())
        			);
        
        given()
    		.accept(MediaType.APPLICATION_JSON).
    		when()
    		.delete(MatchEndpoint.BASE_PATH + "/" + matchId.toString()).
	    then()
	    	.statusCode(202)
	    	.assertThat()
	    	.body(
	    			is(emptyOrNullString())
	    			);
	    	
    }
    
    @Test
    public void testUpdateAMatchShouldNotModifyItsId() {
    	Match newMatch = new Match();
        newMatch.setPlayerOneScore(2);
        newMatch.setPlayerTwoScore(1);
        
        Match updatedMatch = new Match();
        updatedMatch.setId(matchId);
        updatedMatch.setPlayerOneScore(2);
        updatedMatch.setPlayerTwoScore(1);
        
        
        JsonObject newMatchJSON = Json.createObjectBuilder()
				.add("playerOneScore", 2)
				.add("playerTwoScore", 1)
				.build();
        
        when(matchService.updateMatch(matchId.toString(), newMatch)).thenReturn(updatedMatch);
        
        given()
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.body(newMatchJSON.toString()).
	    when()
	    	.put(MatchEndpoint.BASE_PATH + "/" + matchId.toString()).
	    then()
	    	.statusCode(200)
	    	.assertThat()
	    	.body(
	    			"id", equalTo(matchId.intValue()),
	    			"playerOneScore", equalTo(2),
	    			"playerTwoScore", equalTo(1)
	    			);
    }
    
    @Test
    public void testAddMatchShouldReturnTheURLofTheNewMatch() {
    	Player p1 = new Player(); 
    	p1.setId((long)(1));
    	Player p2 = new Player(); 
    	p2.setId((long)(2));
    	
    	Match createdMatch = new Match();
    	createdMatch.setId((long)(3));
    	createdMatch.setPlayerOneScore(1);
    	createdMatch.setPlayerTwoScore(2);
    	createdMatch.setPlayerOne(p1);
    	createdMatch.setPlayerTwo(p2);
    	
    	when(matchService.createMatch(any(Match.class))).thenReturn(createdMatch);
    	
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
    			.header("Location", response -> endsWith(MatchEndpoint.BASE_PATH + "/" + createdMatch.getId().toString()));
    }
    
}