package com.riccardomalavolti.arcano.security;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.SecurityContext;

import com.riccardomalavolti.arcano.model.User;

public class TokenBasedSecurityContext implements SecurityContext {

    private AuthenticatedUser user;
    private String token;
    private final boolean secure;
    
    private class AuthenticatedUser implements Principal {
    	
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

    public TokenBasedSecurityContext(User user, String token, boolean secure) {
        this.user = new AuthenticatedUser(user);
        this.token = token;
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String s) {
        return user.getAuthorities().contains(s);
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }

    public String getAuthenticationTokenDetails() {
        return token;
    }
}