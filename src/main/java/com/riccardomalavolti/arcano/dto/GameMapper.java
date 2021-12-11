package com.riccardomalavolti.arcano.dto;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

import com.riccardomalavolti.arcano.model.Game;

public class GameMapper {
	
	private static ModelMapper mapper = new ModelMapper();
	
	private GameMapper() {
		throw new IllegalStateException("Utility class");
	}
	
	public static GameDetails toGameDetails(Game game) {
		return mapper.map(game, GameDetails.class);
	}

	public static List<GameDetails> toGameDetailsList(List<Game> gameList){
		return gameList.stream().map(GameMapper::toGameDetails).collect(Collectors.toList());
	}

	public static GameBrief toGameBrief(Game game){
		return mapper.map(game, GameBrief.class);
	}

	public static List<GameBrief> toGameBriefList(List<Game> gameList){
		return gameList.stream().map(GameMapper::toGameBrief).collect(Collectors.toList());
	}

}
