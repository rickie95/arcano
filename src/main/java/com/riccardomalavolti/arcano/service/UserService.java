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
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.UserRepository;
import com.riccardomalavolti.arcano.security.PasswordHash;

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
			throw new BadRequestException("No user provided");
		
		if(user.getUsername() == null || user.getUsername().isEmpty())
			throw new BadRequestException("Username can't be empty");
		
		if(user.getPassword() == null || user.getPassword().isEmpty())
			throw new BadRequestException("Password can't be empty.");
		
		try {
			getUserByUsername(user.getUsername());
		}catch(NotFoundException ex) {
			user.setPassword(PasswordHash.hash(user.getPassword()));
			return UserMapper.toUserDetails(userRepo.addNewUser(user));
		}
		
		throw new ConflictException("An user with this username already exists.");
	}

	public UserDetails updateUser(Long userId, User user, final String requesterUsername) {
		User requestedUser = getUserById(userId);		
		authorization.verifyOwnershipOf(requestedUser, requesterUsername);
		user.setId(requestedUser.getId());
		if(user.getPassword() != null)
			user.setPassword(PasswordHash.hash(user.getPassword()));
		return UserMapper.toUserDetails(userRepo.mergeUser(user));
	}

	public UserDetails deleteUser(Long userId, final String requesterUsername) {
		User requestedUser = getUserById(userId);
		authorization.verifyOwnershipOf(requestedUser, requesterUsername);
		return UserMapper.toUserDetails(userRepo.removeUser(requestedUser));
	}

}
