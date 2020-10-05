package com.riccardomalavolti.arcano.security;

import static javax.ws.rs.Priorities.AUTHENTICATION;

import java.io.IOException;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.AuthenticationService;
import com.riccardomalavolti.arcano.service.PlayerService;

@Provider
@Dependent
@Priority(AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@Inject
	AuthenticationService authService;

	@Inject
	PlayerService userService;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String authenticationToken = authorizationHeader.substring(7);
			handleTokenBasedAuthentication(authenticationToken, requestContext);
		}

	}

	private void handleTokenBasedAuthentication(String authenticationToken, ContainerRequestContext requestContext) {

		String username = authService.parseToken(authenticationToken);
		User user = userService.getUserByUsername(username);

		boolean isSecure = requestContext.getSecurityContext().isSecure();
		SecurityContext securityContext = new TokenBasedSecurityContext(user, authenticationToken, isSecure);
		requestContext.setSecurityContext(securityContext);
	}

}
