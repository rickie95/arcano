package com.riccardomalavolti.arcano.endpoints;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import io.restassured.RestAssured;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.riccardomalavolti.arcano.model.Match;
import com.riccardomalavolti.arcano.service.MatchService;


public class MatchEnpointTest extends JerseyTest {

    private static final String MATCHES = "matches";

    @Mock private MatchService matchService;

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
            .get(MATCHES).
        then()
            .statusCode(200)
            .assertThat()
            .body(
                "[0].id", equalTo(match1.getId().intValue()),
                "[1].id", equalTo(match2.getId().intValue())
            );
    }
    
}