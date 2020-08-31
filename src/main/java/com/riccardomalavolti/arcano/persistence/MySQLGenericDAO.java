package com.riccardomalavolti.arcano.persistence;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Default
public class MySQLGenericDAO<T> implements GenericDAO<T> {

	public static final String SELECT_ALL_FROM_ENTITY_TABLE = "SELECT entity FROM :entityTableName";
	public static final String ENTITY_TABLE_NAME_PARAMETER = "entityTableName";
	
	@PersistenceContext
	EntityManager em;
	
	private TypedQuery<T> query;
	
	private Class<T> persistentClass;
	
	public void setClass(Class<T> persistenceClass) {
		this.persistentClass = persistenceClass;
	}
	
	public void checkIfInitialized() {
		if(persistentClass == null)
			throw new IllegalStateException("No class has been specified, use setClass()");
	}
	
	@Override
	@Transactional
	public T findById(Long id) {
		checkIfInitialized();
		
		if (id == null)
			throw new IllegalArgumentException();
		
		return em.find(persistentClass, id);
	}

	@Override
	@Transactional
	public List<T> findAll() {
		checkIfInitialized();
		
		return em.createQuery(SELECT_ALL_FROM_ENTITY_TABLE, persistentClass)
				.setParameter(ENTITY_TABLE_NAME_PARAMETER, persistentClass.toString())
				.getResultList();
	}
	
	@Override
	@Transactional
	public T persist(T entity){
		checkIfInitialized();
		em.persist(entity);
		return entity;
	}

	@Override
	@Transactional
	public T merge(T entity){
		checkIfInitialized();
		
		return em.merge(entity); 
	}

	@Override
	@Transactional
	public T delete(T entity) {
		checkIfInitialized();
		if(!em.contains(entity)) 
			em.merge(entity);	
		em.remove(entity);
		return entity;
	}

	@Override
	@Transactional
	public List<T> executeArbitraryQuery(String query) {
		checkIfInitialized();
		return em.createQuery(query, persistentClass)
				.getResultList();
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
}
