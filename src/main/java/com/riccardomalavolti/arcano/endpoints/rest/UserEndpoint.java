package com.riccardomalavolti.arcano.endpoints.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.security.PermitAll;
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
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.riccardomalavolti.arcano.dto.UserBrief;
import com.riccardomalavolti.arcano.dto.UserDetails;
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
	public List<UserBrief> getUserList() {
		return userService.getAllUsers();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response getUserById(@PathParam("id") Long id, @Context UriInfo uriInfo) {
		UserDetails user = userService.getUserDetailsById(id);
		return Response.ok(user)
			.links(user.getLinks(uriInfo.getBaseUri().toString()).toArray(Link[]::new))
			.build();
	}
	
	@GET
	@Path("/byUsername/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response getUserByUsername(@PathParam("username") String username, @Context UriInfo uriInfo) {
		UserDetails user = userService.getUserDetailsByUsername(username);
		return Response.ok(user)
			.links(user.getLinks(uriInfo.getBaseUri().toString()).toArray(Link[]::new))
			.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response addNewUser(User p, @Context UriInfo uriInfo) throws URISyntaxException {
		UserDetails saved = userService.addNewUser(p);
		return Response.created(new URI(uriInfo.getAbsolutePath() + "/" + saved.getId()))
				.entity(saved)
				.build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response updateUser(@PathParam("id") Long userId, User updatedPlayer) {
		return Response.ok(userService.updateUser(userId, updatedPlayer, getRequester()))
				.build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response deleteUser(@PathParam("id") Long userId) {		
		return Response.accepted(userService.deleteUser(userId, getRequester())).build();
	}
	
	private String getRequester() {
		return context.getUserPrincipal().getName();
	}

}
