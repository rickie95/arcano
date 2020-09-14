package com.riccardomalavolti.arcano.persistence;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Default
public class MySQLGenericDAO<T> implements GenericDAO<T> {

	public static final String SELECT_ALL_FROM_ENTITY_TABLE = "FROM %s";
	
	@PersistenceContext
	EntityManager em;
		
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
		return em.createQuery(
				String.format(SELECT_ALL_FROM_ENTITY_TABLE, 
						persistentClass.getName()), persistentClass)
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

	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
}
