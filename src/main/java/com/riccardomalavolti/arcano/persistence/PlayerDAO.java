package com.riccardomalavolti.arcano.persistence;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.riccardomalavolti.arcano.model.Player;

@ApplicationScoped
public class PlayerDAO extends MySQLGenericDAO<Player> {
	
	public static final String SELECT_ALL_PLAYERS = "FROM Player";
	
	@Override
	@Transactional
	public List<Player> findAll(){
		return em.createQuery(SELECT_ALL_PLAYERS, Player.class).getResultList();
	}
	
	public Player mergePlayer(Player player) {
		return em.merge(player);
	}

}
