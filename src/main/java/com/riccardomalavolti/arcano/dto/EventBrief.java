package com.riccardomalavolti.arcano.dto;

import java.util.UUID;

public class EventBrief {
	
	UUID id;
	String name;
	
	String eventURI;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setEventURI(String uri) {
		this.eventURI = uri;
	}
	public String getEvenURI() {
		return this.eventURI;
	}
	
	public EventBrief addUri(String uri) {
		this.setEventURI(uri);
		return this;
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
		EventBrief other = (EventBrief) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
