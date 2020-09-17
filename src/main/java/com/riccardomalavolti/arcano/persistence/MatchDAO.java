package com.riccardomalavolti.arcano.persistence;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.riccardomalavolti.arcano.model.Match;

@ApplicationScoped
public class MatchDAO extends MySQLGenericDAO<Match> {

	public static final String SELECT_ALL_MATCHES = "FROM Match";
	
	@Override
	@Transactional
	public List<Match> findAll(){
		return em.createQuery(SELECT_ALL_MATCHES, Match.class).getResultList();
	}
}
