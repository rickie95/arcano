package com.riccardomalavolti.arcano.persistence;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;



public interface GenericDAO<T> {
	
	public void setClass(Class<T> persistenceClass);
	public T findById(UUID id);
	public List<T> findAll();
	
	@Transactional public T persist(T entity);
	@Transactional public T merge(T entity);
	@Transactional public T delete(T entity);	
	
}
