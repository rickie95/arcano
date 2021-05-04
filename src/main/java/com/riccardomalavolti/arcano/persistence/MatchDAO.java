package com.riccardomalavolti.arcano.persistence;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.riccardomalavolti.arcano.model.Match;

@ApplicationScoped
public class MatchDAO extends MySQLGenericDAO<Match> {

	public static final String SELECT_ALL_MATCHES = String.format("FROM %s", Match.ENTITY_NAME);
	public static final String MATCHES_OF_EVENT = String.format("FROM %s WHERE %s.eventId = :eventId", Match.ENTITY_NAME, Match.ENTITY_NAME);
	
	public MatchDAO() {
		super.setClass(Match.class);
	}
	
	@Override
	@Transactional
	public List<Match> findAll(){
		return em.createQuery(SELECT_ALL_MATCHES, Match.class).getResultList();
	}

	@Transactional 
	public List<Match> findMatchOfEvent(UUID eventId) {
		return em.createQuery(MATCHES_OF_EVENT, Match.class).setParameter("eventId", eventId).getResultList();
	}
}
