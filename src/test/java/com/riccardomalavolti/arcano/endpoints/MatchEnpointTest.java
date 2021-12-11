package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.riccardomalavolti.arcano.dto.GameMapper;
import com.riccardomalavolti.arcano.dto.MatchBrief;
import com.riccardomalavolti.arcano.dto.MatchDetails;
import com.riccardomalavolti.arcano.dto.MatchMapper;
import com.riccardomalavolti.arcano.endpoints.rest.MatchEndpoint;
import com.riccardomalavolti.arcano.model.Event;
import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.MatchService;

import io.restassured.RestAssured;


public class MatchEnpointTest extends JerseyTest {

    private UUID matchId;

    @Mock private MatchService matchService;
    @Mock private SecurityContext securityContextMock;

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
            }).register(new ContainerRequestFilter() {
				@Override
				public void filter(final ContainerRequestContext containerRequestContext) throws IOException {
					containerRequestContext.setSecurityContext(securityContextMock);
				}
			});
    }

    @Before
    public void configureRestAssured() {
        RestAssured.baseURI = getBaseUri().toString();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Before
    public void initFields(){
        matchId = UUID.randomUUID();
    }

    @Test
    public void getMatchList() {
        Match match1 = new Match();
        match1.setId(UUID.randomUUID());

        Match match2 = new Match();
        match2.setId(UUID.randomUUID());

        List<Match> matchList = new ArrayList<>(Arrays.asList(match1, match2));
        List<MatchBrief> matchBriefList = MatchMapper.toMatchBrief(matchList);
        
        when(matchService.getAllMatches()).thenReturn(matchBriefList);

        given()
            .accept(MediaType.APPLICATION_JSON).
        when()
            .get(MatchEndpoint.BASE_PATH).
        then()
            .statusCode(200)
            .assertThat()
            .body(
                "[0].id", equalTo(match1.getId().toString()),
                "[1].id", equalTo(match2.getId().toString())
            );
    }
    
    @Test
    public void testGetMatchShouldReturnTheDesiredMatch() {
    	Match match1 = new Match();
        match1.setId(matchId);
        match1.setPlayerOne(new User(UUID.randomUUID()));
        match1.setPlayerTwo(new User(UUID.randomUUID()));
        match1.setParentEvent(new Event(UUID.randomUUID()));

        Game g = new Game((long)40, match1);
        Map<UUID, Short> gp = new HashMap<UUID, Short>();
        gp.put(match1.getPlayerOne().getId(), (short)20);
        gp.put(match1.getPlayerTwo().getId(), (short)20);
        g.setGamePoints(gp);

        List<Game> gameList = new ArrayList<Game>(Arrays.asList(
            g,
            new Game((long)41, match1),
            new Game((long)42, match1)
        ));
        
        match1.setGameList(gameList);
        MatchDetails matchDetails = MatchMapper.toMatchDetails(match1);
        
        when(matchService.getMatchDetailsById(matchId)).thenReturn(matchDetails);
        
        given()
        	.accept(MediaType.APPLICATION_JSON).
        when()
        	.get(MatchEndpoint.BASE_PATH + "/" + matchId.toString()).
        then()
        	.statusCode(200)
        	.assertThat()
        	.body(
        			"id", equalTo(matchId.toString()),
                    "gameList.id", hasItems(40, 41, 42)
        			);
    }
    
    @Test
    public void testGetMatchListForEventShouldReturnMatchBriefList() {
    	 Event parentEvent = new Event(UUID.randomUUID());
    	
    	 Match match1 = new Match(UUID.randomUUID());
         match1.setParentEvent(parentEvent);

         Match match2 = new Match(UUID.randomUUID());
         match2.setParentEvent(parentEvent);

         List<Match> matchList = new ArrayList<>(Arrays.asList(match1, match2));
         
         when(matchService.getMatchListForEvent(parentEvent.getId())).thenReturn(MatchMapper.toMatchDetails(matchList));

         given()
             .accept(MediaType.APPLICATION_JSON).
         when()
             .get(MatchEndpoint.BASE_PATH + String.format("/ofEvent/%s", parentEvent.getId())).
         then()
             .statusCode(200)
             .assertThat()
             .body(
                 "[0].id", equalTo(match1.getId().toString()),
                 "[0].parentEvent.id", equalTo(parentEvent.getId().toString()),
                 "[1].id", equalTo(match2.getId().toString()),
                 "[1].parentEvent.id", equalTo(parentEvent.getId().toString())
             );
    }
   
    
    @Test
    public void testAddMatchShouldReturnTheURLofTheNewMatch() {
    	User p1 = new User(); 
    	p1.setId(UUID.randomUUID());
    	User p2 = new User(); 
    	p2.setId(UUID.randomUUID());
    	
    	Match createdMatch = new Match();
    	createdMatch.setId(UUID.randomUUID());
    	createdMatch.setPlayerOneScore(1);
    	createdMatch.setPlayerTwoScore(2);
    	createdMatch.setPlayerOne(p1);
    	createdMatch.setPlayerTwo(p2);
    	
    	when(matchService.createMatch(any(Match.class))).thenReturn(MatchMapper.toMatchDetails(createdMatch));
    	
    	 JsonObject newMatchJSON = Json.createObjectBuilder()
 				.add("playerOneScore", 1)
 				.add("playerTwoScore", 2)
 				.add("playerOne", Json.createObjectBuilder().add("id", p1.getId().toString()).build())
 				.add("playerTwo", Json.createObjectBuilder().add("id", p2.getId().toString()).build())
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
    					"id", equalTo(createdMatch.getId().toString()),
    					"playerOneScore", equalTo(1),
    					"playerTwoScore", equalTo(2),
    					"playerOne.id", equalTo(p1.getId().toString()),
    					"playerTwo.id", equalTo(p2.getId().toString())
    					)
    			.header("Location", response -> endsWith(MatchEndpoint.BASE_PATH + "/" + createdMatch.getId().toString()));
    }
    
}