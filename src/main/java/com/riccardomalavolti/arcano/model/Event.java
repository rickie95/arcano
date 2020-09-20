package com.riccardomalavolti.arcano.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Event {
	
	@Id
	@GeneratedValue
	private Long id;

	public void setId(long id) {
		this.id = id;
	}

}
