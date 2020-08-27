package com.riccardomalavolti.arcano.persistence;

import java.util.List;

import javax.transaction.Transactional;



public interface GenericDAO<T> {
	
	public void setClass(Class<T> persistenceClass);
	public T findById(Long id);
	public List<T> findAll();
	
	@Transactional public T persist(T entity);
	@Transactional public T merge(T entity);
	@Transactional public T delete(T entity);
	
	@Transactional public Object executeArbitraryQuery(String query);
	
	
}
