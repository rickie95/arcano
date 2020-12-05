package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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

import com.riccardomalavolti.arcano.endpoints.rest.GameEndpoint;
import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.service.GameService;

import io.restassured.RestAssured;

public class GameEndpointTest extends JerseyTest {
	
	@Mock private GameService gameService;
	
	@SuppressWarnings("deprecation")
	@Override
	protected Application configure(){
        MockitoAnnotations.initMocks(this);
        forceSet(TestProperties.CONTAINER_PORT, "0");
        return new ResourceConfig(GameEndpoint.class)
            .register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(gameService).to(GameService.class);
                }
            });
    }
	
	@Before
	public void configureRestAssured() {
		RestAssured.baseURI = getBaseUri().toString();
	}
	
	@Test
	public void testGetGameById() {
		Long gameID = (long)1;
		Game gameOne = new Game();
		gameOne.setId(gameID);
		gameOne.setEnded(false);
		gameOne.setPointsForPlayer((long)2, (short)20);
		gameOne.setPointsForPlayer((long)3, (short)20);
		
		when(gameService.getGameById(gameID)).thenReturn(gameOne);
		
		given()
			.accept(MediaType.APPLICATION_JSON).
		when()
			.get(GameEndpoint.BASE_PATH + "/" + gameID.toString()).
		then()
			.statusCode(200)
			.assertThat()
				.body(
						"id", equalTo(gameID.intValue())
						);
	}
	
	@Test
	public void testCreateNewGame() {		
		Game newGame = new Game();
		newGame.setEnded(false);
		newGame.setPointsForPlayer((long)2, (short)20);
		newGame.setPointsForPlayer((long)3, (short)20);
		
		Game createdGame = new Game();
		createdGame.setId((long)1);
		createdGame.setEnded(false);
		createdGame.setPointsForPlayer((long)2, (short)20);
		createdGame.setPointsForPlayer((long)3, (short)20);
		
		when(gameService.createGame(newGame)).thenReturn(createdGame);
		
		Map<Long, Short> gameP = newGame.getGamePoints();
		
		JsonObjectBuilder builder = Json.createObjectBuilder();
		for (Entry<Long, Short> entry : gameP.entrySet()) {
			builder.add(entry.getKey().toString(), entry.getValue().toString());
		}
		JsonObject gamePoints = builder.build();

		JsonObject gameJson = Json.createObjectBuilder()
					 .add("isEnded", newGame.isEnded())
					 .add("gamePoints", gamePoints)
				 .build();		
		
		given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(gameJson.toString()).
		when()
			.post(GameEndpoint.BASE_PATH).
		then()
			.statusCode(201)
			.assertThat()
				.body(
						"id", equalTo(1)
					 )
				.header("Location", 
						response -> endsWith(GameEndpoint.BASE_PATH + "/"+ createdGame.getId()));
	}

}
