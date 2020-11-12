package com.riccardomalavolti.arcano.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.riccardomalavolti.arcano.model.User;

public class UserMapper {
	
private static ModelMapper mapper = new ModelMapper();
	
	private UserMapper() {
		throw new IllegalStateException("Utility class");
	}
	
	public static UserBrief toUserBrief(User user) {
		return mapper.map(user, UserBrief.class);
	}
	
	public static List<UserBrief> toUserBriefList(List<User> userList){
		return userList.stream().map(UserMapper::toUserBrief).collect(Collectors.toList());
	}

	public static UserDetails toUserDetails(User user) {
		return mapper.map(user, UserDetails.class);
	}
	
	public static List<UserDetails> toUserDetailsList(List<User> userList){
		return userList.stream().map(UserMapper::toUserDetails).collect(Collectors.toList());
	}

}
