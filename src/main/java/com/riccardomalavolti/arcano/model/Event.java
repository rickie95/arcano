package com.riccardomalavolti.arcano.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Type;

import com.riccardomalavolti.arcano.exceptions.ConflictException;

@Entity
public class Event implements Ownable {

	@Id
	@Type(type="uuid-char")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "EVENT_PLAYERS", joinColumns = { @JoinColumn(name = "Event_id") }, inverseJoinColumns = {
			@JoinColumn(name = "Player_id") })
	private Set<User> playerList;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "EVENT_JUDGES", joinColumns = { @JoinColumn(name = "Event_id") }, inverseJoinColumns = {
			@JoinColumn(name = "Judge_id") })
	private Set<User> judgeList;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "EVENT_ADMINS", joinColumns = { @JoinColumn(name = "Event_id") }, inverseJoinColumns = {
			@JoinColumn(name = "Admin_id") })
	private Set<User> adminList;

	private LocalDateTime startingTime;
	private EventStatus status;


	public Event() {
		playerList = new HashSet<>();
		adminList = new HashSet<>();
		judgeList = new HashSet<>();
	}
	
	public Event(UUID id) {
		this();
		this.setId(id);
	}
	
	public Event(UUID id, String name) {
		this(id);
		this.setName(name);
	}

	public User enrollPlayer(User player) {
		if (playerList.add(player))
			return player;

		throw new ConflictException("Player is already enrolled");
	}
	
	public void removePlayer(User player) {
		playerList.remove(player);
	}

	public User addJudge(User judge) {
		if (judgeList.add(judge))
			return judge;

		throw new ConflictException("Judge is already enrolled");
	}

	public User addAdmin(User admin) {
		if (adminList.add(admin))
			return admin;

		throw new ConflictException(String.format("%s is already an admin", admin.getUsername()));
	}

	@Override
	public boolean isOwnedBy(User user) {
		return adminList.contains(user);
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public UUID getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
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

	public List<User> getPlayerList() {
		return new ArrayList<>(playerList);
	}

	public List<User> getJudgeList() {
		return new ArrayList<>(judgeList);
	}

	public List<User> getAdminList() {
		return new ArrayList<>(adminList);
	}
	
	public LocalDateTime getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(LocalDateTime startingTime) {
		this.startingTime = startingTime;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", playerList=" + playerList + ", judgeList=" + judgeList
				+ ", adminList=" + adminList + "]";
	}

}
