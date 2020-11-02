package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.exceptions.ConflictException;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.UserRepository;

@RequestScoped
@Default
public class UserService {
	
	@Inject
	private UserRepository userRepo;
	
	@Inject 
	private AuthorizationService authorization;

	public List<User> getAllUsers() {
		return userRepo.getAllUsers();
	}

	public User getUserById(Long userId) {
		return userRepo
				.getUserById(userId)
				.orElseThrow(() -> new NotFoundException("No player exists with id" + userId));
	}
	
	public User getUserByUsername(String username) {
		return userRepo.getUserByUsername(username)
				.orElseThrow(() -> new NotFoundException("No player exists with username " + username));
	}

	public User addNewUser(User user) {
		try {
			getUserByUsername(user.getUsername());
		}catch(NotFoundException ex) {
			return userRepo.addNewUser(user);
		}
		
		throw new ConflictException();
	}

	public User updateUser(Long userId, User user, final String requesterUsername) {
		User requestedUser = getUserById(userId);		
		authorization.verifyOwnershipOf(requestedUser, requesterUsername);
		user.setId(requestedUser.getId());
		return userRepo.mergeUser(user);
	}

	public User deleteUser(Long userId, final String requesterUsername) {
		User requestedUser = getUserById(userId);
		authorization.verifyOwnershipOf(requestedUser, requesterUsername);
		return userRepo.removeUser(requestedUser);
	}
	

}
