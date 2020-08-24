package com.riccardomalavolti.arcano.persistence;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Default
public class MySQLGenericDAO<T> implements GenericDAO<T> {

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
	
	public T findById(Long id) {
		checkIfInitialized();
		
		if (id == null)
			throw new IllegalArgumentException();
		
		return em.find(persistentClass, id);
	}

	public List<T> findAll() {
		checkIfInitialized();
		
		query = em.createQuery("SELECT * FROM :tableName;", persistentClass)
				.setParameter("tableName", persistentClass);
		
		return query.getResultList();
	}
	
	@Transactional
	public T persist(T entity){
		checkIfInitialized();
		em.persist(entity);
		return entity;
	}

	@Override
	public T merge(T entity){
		checkIfInitialized();
		return em.merge(entity);
	}

	@Override
	public void delete(T entity) {
		checkIfInitialized();
		em.remove(entity);
	}

	@Override
	public List<T> executeArbitraryQuery(String query) {
		checkIfInitialized();
		return em.createQuery(query, persistentClass)
				.getResultList();
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
}
