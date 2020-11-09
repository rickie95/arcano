package com.riccardomalavolti.arcano.dto;

import java.util.Set;

public class EventDetails {
	
	Long id;
	String name;
	
	Set<UserDTO> playerList;
	Set<UserDTO> judgeList;
	Set<UserDTO> adminList;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<UserDTO> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(Set<UserDTO> playerList) {
		this.playerList = playerList;
	}
	public Set<UserDTO> getJudgeList() {
		return judgeList;
	}
	public void setJudgeList(Set<UserDTO> judgeList) {
		this.judgeList = judgeList;
	}
	public Set<UserDTO> getAdminList() {
		return adminList;
	}
	public void setAdminList(Set<UserDTO> adminList) {
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
		EventDetails other = (EventDetails) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
