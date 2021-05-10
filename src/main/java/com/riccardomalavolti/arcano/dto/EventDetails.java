package com.riccardomalavolti.arcano.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.Link;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.riccardomalavolti.arcano.model.EventStatus;

public class EventDetails implements RESTResource {
	
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
	
	UUID id;
	String name;
	
	Set<UserBrief> playerList;
	Set<UserBrief> judgeList;
	Set<UserBrief> adminList;
	
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	LocalDateTime startingTime;
	
	EventStatus eventStatus;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<UserBrief> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(Set<UserBrief> playerList) {
		this.playerList = playerList;
	}
	public Set<UserBrief> getJudgeList() {
		return judgeList;
	}
	public void setJudgeList(Set<UserBrief> judgeList) {
		this.judgeList = judgeList;
	}
	public Set<UserBrief> getAdminList() {
		return adminList;
	}
	public void setAdminList(Set<UserBrief> adminList) {
		this.adminList = adminList;
	}
	
	public LocalDateTime getStartingTime() {
		return startingTime;
	}
	
	public void setStartingTime(LocalDateTime startingTime) {
		this.startingTime = startingTime;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}
	@Override
	public List<Link> getLinks(String absoluteBasePath) {
		List<Link> links = new ArrayList<Link>();
		// SELF
		links.add(Link.fromUri("{base_uri}/events/{id}")
			.rel("self").type("text/plain")
			.build(absoluteBasePath, this.id));

		// Matches
		links.add(Link.fromUri("{base_uri}/matches/ofEvent/{id}")
			.rel("matches").type("text/plain")
			.build(absoluteBasePath, this.id));

		// Players
		links.add(Link.fromUri("{base_uri}/events/{id}/players")
			.rel("players").type("text/plain")
			.build(absoluteBasePath, this.id));

		links.add(Link.fromUri("{base_uri}/events/{id}/judges")
			.rel("self").type("text/plain")
			.build(absoluteBasePath, this.id));
		return links;
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
