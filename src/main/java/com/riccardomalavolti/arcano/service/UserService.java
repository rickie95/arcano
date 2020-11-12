package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.dto.UserBrief;
import com.riccardomalavolti.arcano.dto.UserDetails;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.exceptions.ConflictException;
import com.riccardomalavolti.arcano.model.Role;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.UserRepository;

@RequestScoped
@Default
public class UserService {
	
	@Inject
	private UserRepository userRepo;
	
	@Inject 
	private AuthorizationService authorization;
	
	public UserService() {}
	
	public UserService(UserRepository userRepo, AuthorizationService authService) {
		this();
		this.userRepo = userRepo;
		this.authorization = authService;
	}

	public List<UserBrief> getAllUsers() {
		return UserMapper.toUserBriefList(userRepo.getAllUsers());
	}
	
	User getUserById(Long userId) {
		return userRepo
				.getUserById(userId)
				.orElseThrow(() -> new NotFoundException("No player exists with id" + userId));
	}
	
	User getUserByUsername(String username) {
		return userRepo.getUserByUsername(username)
				.orElseThrow(() -> new NotFoundException("No player exists with username " + username));
	}

	public UserDetails getUserDetailsById(Long userId) {
		return UserMapper.toUserDetails(getUserById(userId));
	}
	
	public UserDetails getUserDetailsByUsername(String username) {
		return UserMapper.toUserDetails(getUserByUsername(username));
	}

	public UserDetails addNewUser(User user) {
		if(user == null)
			throw new BadRequestException();
		try {
			getUserByUsername(user.getUsername());
		}catch(NotFoundException ex) {
			return UserMapper.toUserDetails(userRepo.addNewUser(user));
		}
		
		throw new ConflictException();
	}

	public UserDetails updateUser(Long userId, User user, final String requesterUsername) {
		User requestedUser = getUserById(userId);		
		authorization.verifyOwnershipOf(requestedUser, requesterUsername);
		user.setId(requestedUser.getId());
		return UserMapper.toUserDetails(userRepo.mergeUser(user));
	}

	public UserDetails deleteUser(Long userId, final String requesterUsername) {
		User requestedUser = getUserById(userId);
		authorization.verifyOwnershipOf(requestedUser, requesterUsername);
		return UserMapper.toUserDetails(userRepo.removeUser(requestedUser));
	}
	
	public User getUserWithRoleById(Long userId, Role role) {
		User user = getUserById(userId);
		if(role != null && user.getRole() != role)
			throw new IllegalArgumentException(String.format("Role %s does not match or is invalid.", role));
		return user;
	}

}
