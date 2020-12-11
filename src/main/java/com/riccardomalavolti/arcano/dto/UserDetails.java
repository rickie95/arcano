package com.riccardomalavolti.arcano.dto;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import com.riccardomalavolti.arcano.model.Role;

public class UserDetails implements RESTResource {
	
	Long id;
	String username;
	
	String name;
	String surname;
	Role role;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public List<Link> getLinks(String absoluteBasePath) {
		List<Link> links = new ArrayList<Link>();
		links.add(Link.fromUri("{base_uri}/users/{id}")
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
		UserDetails other = (UserDetails) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
