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

	public Optional<User> getPlayerById(Long playerId) {
		return Optional.ofNullable(userDAO.findById(playerId));
	}

	public User addPlayer(User p) {
		return userDAO.persist(p);
	}

	public Optional<User> removePlayer(User player) {
		return Optional.ofNullable(userDAO.delete(player));
	}

	public Optional<User> mergePlayer(User player) {
		return Optional.ofNullable(userDAO.merge(player));		
	}

	public void authenticate(User user) {
		userDAO.authenticateUser(user);
	}

	public Optional<User> getUserByUsername(String username) {
		return Optional.ofNullable(userDAO.findUserByUsername(username));
	}
}
