package com.riccardomalavolti.arcano.endpoints.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.riccardomalavolti.arcano.exceptions.AccessDeniedException;
import com.riccardomalavolti.arcano.model.Role;
import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.UserService;

@RequestScoped
@Path(UserEndpoint.ENDPOINT_PATH)
public class UserEndpoint {
	
	public static final String ENDPOINT_PATH = "users";
	
	@Context
	SecurityContext context;
	
	@Inject
	private UserService userService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public List<User> getUserList() {
		return userService.getAllUsers();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Role.Values.JUDGE_VALUE, Role.Values.ADMIN_VALUE})
	public User getUserById(@PathParam("id") String id) {
		return userService.getUserById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response addNewUser(User p, @Context UriInfo uriInfo) throws URISyntaxException {
		User saved = userService.addNewUser(p);
		return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
				.entity(saved)
				.build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response updateUser(@PathParam("id") String userId, User updatedPlayer) {
		
		verifyOwnershipOf(userId);
		
		return Response
				.ok(userService.updateUser(userId, updatedPlayer))
				.build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response deleteUser(@PathParam("id") String userId) {
		
		verifyOwnershipOf(userId);
		
		return Response.accepted(userService.deleteUser(userId)).build();
	}
	
	private void verifyOwnershipOf(String resourceId) {
		/*
		 * Simply checks if requester is the legitimate owner.
		 */
		User requestedUser = userService.getUserById(resourceId);
		if(!context.getUserPrincipal().getName().equals(requestedUser.getUsername()))
			throw new AccessDeniedException("You are not the owner");
	}


}
