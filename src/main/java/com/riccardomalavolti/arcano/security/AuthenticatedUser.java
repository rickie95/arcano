package com.riccardomalavolti.arcano.security;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.riccardomalavolti.arcano.model.User;

public class AuthenticatedUser implements Principal {
    	
    	private User user;
    	
        public AuthenticatedUser(User user) {
            this.user = user;
        }

        public Set<String> getAuthorities() {
        	Set<String> roles = new HashSet<>();
        	roles.add(user.getRole().toString());
            return Collections.unmodifiableSet(roles);
        }

		@Override
		public String getName() {
			return user.getUsername();
		}

    	
    }