package com.riccardomalavolti.arcano.dto;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.riccardomalavolti.arcano.model.Match;

public class MatchMapper {
	
	private static ModelMapper mapper = new ModelMapper();
	
	private MatchMapper() {}
	
	public static MatchBrief toMatchBrief(Match match) {
		return mapper.map(match, MatchBrief.class);
	}
	
	public static List<MatchBrief> toMatchBrief(List<Match> matchList) {
		return matchList.stream().map(MatchMapper::toMatchBrief).collect(Collectors.toList());
	}
	
	public static MatchDetails toMatchDetails(Match match) {
		MatchDetails mDetails = new MatchDetails();
		mDetails.setId(match.getId());
		mDetails.setParentEvent(
			match.getParentEvent() != null ? 
				EventMapper.toEventDetails(match.getParentEvent()) : 
				null
			);
		mDetails.setPlayerOne(UserMapper.toUserDetails(match.getPlayerOne()));
		mDetails.setPlayerTwo(UserMapper.toUserDetails(match.getPlayerTwo()));
		mDetails.setWinner(UserMapper.toUserDetails(match.getWinner()));
		mDetails.setPlayerOneScore(match.getPlayerOneScore());
		mDetails.setPlayerTwoScore(match.getPlayerTwoScore());
		mDetails.setGameList(
			match.getGameList() != null ? 
				match.getGameList().stream().map(GameMapper::toGameBrief).collect(Collectors.toList()) : 
				null
			);
		mDetails.setMatchEnded(match.isMatchEnded());
		return mDetails;
	}
	
	public static List<MatchDetails> toMatchDetails(List<Match> matchList) {
		return matchList.stream().map(MatchMapper::toMatchDetails).collect(Collectors.toList());
	}
	
}

	
