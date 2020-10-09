package com.riccardomalavolti.arcano.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.riccardomalavolti.arcano.exceptions.ConflictException;

@Entity
public class Event implements Ownable{
	
	@Id
	@GeneratedValue
	private Long id;

	private String name;
	
	@Transient private Set<User> playerList;
	@Transient private Set<User> judgeList;
	@Transient private Set<User> adminList;

	
	public void setId(long id) {
		this.id = id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPlayerList(Set<User> playerList) {
		this.playerList = playerList;
	}

	public void setJudgeList(Set<User> judgeList) {
		this.judgeList = judgeList;
	}
	
	public void setAdminList(Set<User> adminList) {
		this.adminList = adminList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public User enrollPlayer(User player) {
		if(playerList.add(player))
			return player;
		
		throw new ConflictException("Player is already enrolled");
	}
	
	public List<User> getPlayerList(){
		return new ArrayList<>(playerList);
	}
	
	public User addJudge(User judge) {
		if(judgeList.add(judge))
			return judge;
		
		throw new ConflictException("Judge is already enrolled");
	}
	
	public List<User> getJudgeList(){
		return new ArrayList<>(judgeList);
	}
	
	public User addAdmin(User admin) {
		if(adminList.add(admin))
			return admin;
		
		throw new ConflictException(String.format("%s is already an admin", admin.getUsername()));
	}

	@Override
	public boolean isOwnedBy(User user) {
		return adminList.contains(user);
	}

}
