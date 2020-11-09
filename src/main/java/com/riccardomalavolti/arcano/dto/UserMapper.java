package com.riccardomalavolti.arcano.dto;

import org.modelmapper.ModelMapper;

import com.riccardomalavolti.arcano.model.User;

public class UserMapper {
	
private static ModelMapper mapper;
	
	private UserMapper() {
		throw new IllegalStateException("Utility class");
	}
	
	public static UserDTO toUserDTO(User user) {
		mapper = new ModelMapper();
		return mapper.map(user, UserDTO.class);
	}
	

}
