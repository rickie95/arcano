package com.riccardomalavolti.arcano.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.riccardomalavolti.arcano.model.Match;

public class MatchMapper {
	
	private static ModelMapper mapper = new ModelMapper();
	
	private MatchMapper() {
		throw new IllegalStateException("Utility class");
	}
	
	public static MatchBrief toMatchBrief(Match match) {
		return mapper.map(match, MatchBrief.class);
	}
	
	public static List<MatchBrief> toMatchBrief(List<Match> matchList) {
		return matchList.stream().map(MatchMapper::toMatchBrief).collect(Collectors.toList());
	}
	
	public static MatchDetails toMatchDetails(Match match) {
		return mapper.map(match, MatchDetails.class);
	}
	
	public static List<MatchDetails> toMatchDetails(List<Match> matchList) {
		return matchList.stream().map(MatchMapper::toMatchDetails).collect(Collectors.toList());
	}
	
}

	
