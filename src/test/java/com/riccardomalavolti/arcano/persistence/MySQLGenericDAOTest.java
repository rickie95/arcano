package com.riccardomalavolti.arcano.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MySQLGenericDAOTest {
	
	// Tested with String
	
	@Mock EntityManager em;
	@Mock TypedQuery<String> query;
	
	@InjectMocks
	MySQLGenericDAO<String> dao;
	
	@BeforeEach
	public void setUp() {
		dao.setClass(String.class);
	}

	@Test
	void testCheckInitialization() {
		dao.checkIfInitialized();
		assertEquals(String.class, dao.getPersistentClass());
	}
	
	@Test
	void checkInitializationShouldThrowsAnIllegalStateExIfClassIsNull() {
		dao.setClass(null);
		
		assertThrows(IllegalStateException.class, () -> 
				dao.checkIfInitialized());
	}
	
	@Test
	void findByIdShouldReturnTheObjectWithTheCorrectPrimaryKey() {
		Long id = (long) 1;
		String toBeReturned = "Foo";
		when(em.find(String.class, id)).thenReturn(toBeReturned);
		
		String returnedString = dao.findById(id);
		assertEquals(toBeReturned, returnedString);
	}
	
	@Test
	void findByIdShouldThrowsAnIllegalArgumentExceptionIfANullKeyIsPassed() {
		Long id = null;
		assertThrows(IllegalArgumentException.class, () -> 
			dao.findById(id));
	}
	
	@Test
	void findAllShouldReturnACollectionOfObjects() {
		List<String> list = new ArrayList<String>(Arrays.asList("Foo", "Bar"));
		
		String formattedQuery = String.format(MySQLGenericDAO.SELECT_ALL_FROM_ENTITY_TABLE, String.class.getName());
		
		when(em.createQuery(formattedQuery, String.class))
			.thenReturn(query);
		
		
		when(query.getResultList()).thenReturn(list);
		
		List<String> res = dao.findAll();
		
		assertEquals(list, res);
	}
	
	@Test
	void testPersist() {
		String s = "Foo";
		
		String returned = dao.persist(s);
		
		verify(em).persist(s);
		assertEquals(s, returned);
	}
	
	@Test
	void testDeleteIfEntityIsManaged() {
		String s = "Foo";
		when(em.contains(s)).thenReturn(true);
		dao.delete(s);
		verify(em).remove(s);
	}
	
	@Test
	void testDeleteIfEntityIsNotManagedShouldMergeFirst() {
		String s = "Foo";
		when(em.contains(s)).thenReturn(false);
		when(em.merge(s)).thenReturn(s);
		dao.delete(s);
		verify(em).merge(s);
		verify(em).remove(s);
	}
	
	@Test
	void testMerge() {
		String s = "Foo";
		dao.merge(s);
		verify(em).merge(s);
	}

}
