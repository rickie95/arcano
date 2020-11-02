package com.riccardomalavolti.arcano.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.riccardomalavolti.arcano.model.User;

public class TokenBasedSecurityContext implements SecurityContext {

    private AuthenticatedUser user;
    private String token;
    private final boolean secure;

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