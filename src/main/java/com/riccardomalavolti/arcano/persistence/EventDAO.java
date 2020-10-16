package com.riccardomalavolti.arcano.persistence;

import java.util.List;

import javax.transaction.Transactional;

import com.riccardomalavolti.arcano.model.Event;

public class EventDAO extends MySQLGenericDAO<Event> {

	public static final String SELECT_ALL_EVENTS = "FROM Event";
	
	public EventDAO() {
		super.setClass(Event.class);
	}
	
	@Override
	@Transactional
	public List<Event> findAll(){
		return em.createQuery(SELECT_ALL_EVENTS, Event.class).getResultList();
	}

}
