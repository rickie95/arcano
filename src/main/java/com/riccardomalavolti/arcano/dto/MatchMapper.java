package com.riccardomalavolti.arcano.dto;

import org.modelmapper.ModelMapper;

import com.riccardomalavolti.arcano.model.Match;

public class MatchMapper {
	
	private static ModelMapper mapper;
	
	private MatchMapper() {
		throw new IllegalStateException("Utility class");
	}
	
	public static MatchBrief toMatchBrief(Match match) {
		mapper = new ModelMapper();
		return mapper.map(match, MatchBrief.class);
	}


}
