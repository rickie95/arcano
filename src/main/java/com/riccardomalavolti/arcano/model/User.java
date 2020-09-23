package com.riccardomalavolti.arcano.model;

import javax.persistence.Id;

public class User {
	
	@Id
	private Long id;
	
	private Role role;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public Role getRole() {
		return this.role;
	}

}
