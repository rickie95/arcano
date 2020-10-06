package com.riccardomalavolti.arcano.endpoints.rest;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.security.TokenBasedSecurityContext;
import com.riccardomalavolti.arcano.service.AuthenticationService;

@RequestScoped
@Path("auth")
public class AuthenticationEndpoint {
	
	@Context
    SecurityContext securityContext;
	
	@Inject
	AuthenticationService authService;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response authenticateUser(User user) {
		try {
			
			String token = authService.authenticateUser(user);
			// Return the token on the response
			return Response.ok(token).build();

		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}
	
	@POST
    @Path("refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refresh() {

        String tokenDetails = ((TokenBasedSecurityContext) securityContext).getAuthenticationTokenDetails();
        String token = authService.refreshToken(tokenDetails);

        return Response.ok(token).build();
    }

}
