package com.riccardomalavolti.arcano.persistence;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import com.riccardomalavolti.arcano.model.User;

@ApplicationScoped
public class UserDAO extends MySQLGenericDAO<User> {
	
	public static final String SELECT_ALL_USERS = "FROM User";
	public static final String USER_BY_USERNAME = "FROM User WHERE username = :username";
	
	@Override
	@Transactional
	public List<User> findAll(){
		return em.createQuery(SELECT_ALL_USERS, User.class).getResultList();
	}

	public User findUserByUsername(String username) {
		try {
			return em.createQuery(USER_BY_USERNAME, User.class)
					.setParameter("username", username)
					.getSingleResult();
		}catch(NoResultException ex) {
			return null;
		}	
	}

}
