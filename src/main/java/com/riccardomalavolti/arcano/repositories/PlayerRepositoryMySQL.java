package com.riccardomalavolti.arcano.repositories;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.riccardomalavolti.arcano.model.Player;

@Default
public class PlayerRepositoryMySQL implements PlayerRepository {

	@PersistenceContext
	EntityManager entityManager;

	public List<Player> getAllPlayers() {
		return entityManager.createQuery("SELECT * FROM player;", Player.class).getResultList();
	}

	public Player getPlayerById(Long playerId) {
		return entityManager.find(Player.class, playerId);
	}

	@Transactional
	public boolean commitChanges() {

		Player p1 = new Player();

		p1.setId(Integer.toUnsignedLong(1));
		p1.setName("Mike");

		Player p2 = new Player();
		p2.setId(Integer.toUnsignedLong(2));
		p2.setName("Jack");

		try {
			entityManager.persist(p1);
			entityManager.persist(p2);
		} catch (Exception ex) {
			System.out.println(ex);
			entityManager.getTransaction().rollback();
			return false;
		}
		
		return true;
	}

	@Override
	public void addPlayer(Player p) {
		entityManager.persist(p);
	}

}
