package com.riccardomalavolti.arcano.security;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.riccardomalavolti.arcano.dto.UserDetails;

public class AuthenticatedUser implements Principal {
    	
    	private UserDetails user;
    	
        public AuthenticatedUser(UserDetails user) {
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