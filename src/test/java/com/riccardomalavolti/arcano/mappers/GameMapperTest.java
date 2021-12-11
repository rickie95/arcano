package com.riccardomalavolti.arcano.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import com.riccardomalavolti.arcano.dto.GameDetails;
import com.riccardomalavolti.arcano.dto.GameMapper;
import com.riccardomalavolti.arcano.dto.MatchMapper;
import com.riccardomalavolti.arcano.model.Game;
import com.riccardomalavolti.arcano.model.Match;

import org.junit.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class GameMapperTest {

    @Test
    public void testGameToGameDetails(){
        Match match = new Match(UUID.randomUUID());
        Game game = new Game((long)1);
        game.setParentMatch(match);

        GameDetails gameDTO = GameMapper.toGameDetails(game);

        assertThat(gameDTO.getId()).isEqualTo((long)1);
        assertThat(gameDTO.getParentMatch()).isEqualTo(MatchMapper.toMatchBrief(match));

    }

    
}
