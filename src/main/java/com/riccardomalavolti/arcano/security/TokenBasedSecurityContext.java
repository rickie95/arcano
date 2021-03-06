package com.riccardomalavolti.arcano.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.riccardomalavolti.arcano.dto.UserDetails;

public class TokenBasedSecurityContext implements SecurityContext {

    private AuthenticatedUser user;
    private String token;
    private final boolean secure;

    public TokenBasedSecurityContext(UserDetails user, String token, boolean secure) {
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
        return true;
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