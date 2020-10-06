package com.riccardomalavolti.arcano.repositories;

import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.persistence.UserDAO;

@Default
public class UserRepository {

	final UserDAO userDAO;
	
	@Inject
	public UserRepository(UserDAO playerDAO) {
		this.userDAO = playerDAO;
		this.userDAO.setClass(User.class);
	}

	public List<User> getAllPlayers() {
		return userDAO.findAll();
	}

	public Optional<User> getUserById(Long playerId) {
		return Optional.ofNullable(userDAO.findById(playerId));
	}
	
	public Optional<User> getUserByUsername(String username) {
		return Optional.ofNullable(userDAO.findUserByUsername(username));
	}

	public User addNewUser(User p) {
		return userDAO.persist(p);
	}

	public User removeUser(User player) {
		return userDAO.delete(player);
	}

	public User mergeUser(User player) {
		return userDAO.merge(player);		
	}

	public void authenticate(User user) {
		userDAO.authenticateUser(user);
	}

	
}
