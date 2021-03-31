package com.riccardomalavolti.arcano.repositories;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.persistence.UserDAO;

@Dependent
public class UserRepository {

	final UserDAO userDAO;
	
	@Inject
	public UserRepository(UserDAO userDAO) {
		this.userDAO = userDAO;
		this.userDAO.setClass(User.class);
	}

	public List<User> getAllUsers() {
		return userDAO.findAll();
	}

	public Optional<User> getUserById(Long userId) {
		return Optional.ofNullable(userDAO.findById(userId));
	}
	
	public Optional<User> getUserByUsername(String username) {
		return Optional.ofNullable(userDAO.findUserByUsername(username));
	}

	public User addNewUser(User p) {
		return userDAO.persist(p);
	}

	public User removeUser(User user) {
		return userDAO.delete(user);
	}

	public User mergeUser(User user) {
		return userDAO.merge(user);		
	}
	
}
