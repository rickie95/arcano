package com.riccardomalavolti.arcano.dto;

import org.modelmapper.ModelMapper;

import com.riccardomalavolti.arcano.model.Match;

public class MatchMapper {
	
	private static ModelMapper mapper;
	
	public static MatchDTO toDTO(Match match) {
		mapper = new ModelMapper();
		return mapper.map(match, MatchDTO.class);
	}
	
	public static Match toModel(MatchDTO matchDto) {
		mapper = new ModelMapper();
		return mapper.map(matchDto, Match.class);
	}

}
