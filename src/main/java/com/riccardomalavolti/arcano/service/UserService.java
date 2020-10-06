package com.riccardomalavolti.arcano.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.ws.rs.NotFoundException;

import com.riccardomalavolti.arcano.exceptions.ConflictException;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.UserRepository;

@RequestScoped
@Default
public class UserService {
	
	@Inject
	private UserRepository userRepo;

	public List<User> getAllUsers() {
		return userRepo.getAllPlayers();
	}

	public User getUserById(String userId) {
		return userRepo
				.getUserById(Long.parseLong(userId))
				.orElseThrow(() -> new NotFoundException("No player exist with id" + userId));
	}
	
	public User getUserByUsername(String username) {
		return userRepo.getUserByUsername(username)
				.orElseThrow(() -> new NotFoundException("No player exist with username " + username));
	}

	public User addNewUser(User user) {
		try {
			return userRepo.addNewUser(user);
		}catch(EntityExistsException ex) {
			throw new ConflictException();
		}
	}

	public User updateUser(String userId, User user) {
		User accessedUser = getUserById(userId);		
		
		user.setId(accessedUser.getId());
		return userRepo.mergeUser(user);
	}

	public User deleteUser(String userId) {
		User requestedUser = getUserById(userId);
		return userRepo.removeUser(requestedUser);
	}

}
