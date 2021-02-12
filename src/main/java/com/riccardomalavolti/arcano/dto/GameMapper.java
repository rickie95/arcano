package com.riccardomalavolti.arcano.dto;

import org.modelmapper.ModelMapper;

import com.riccardomalavolti.arcano.model.Game;

public class GameMapper {
	
	private static ModelMapper mapper = new ModelMapper();
	
	private GameMapper() {
		throw new IllegalStateException("Utility class");
	}
	
	public GameDetails toGameDetails(Game game) {
		return mapper.map(game, GameDetails.class);
	}

}
