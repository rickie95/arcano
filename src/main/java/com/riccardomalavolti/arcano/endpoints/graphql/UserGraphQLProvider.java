package com.riccardomalavolti.arcano.endpoints.graphql;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.service.AuthenticationService;
import com.riccardomalavolti.arcano.service.UserService;


@GraphQLApi
public class UserGraphQLProvider {
	
	@Inject UserService userService;

	@Inject AuthenticationService authService;

	@Query("userList")
	@Description("Returns a list of all available users.")
	public List<User> getUsers(){
		return userService.getAllUsers();
	}
	
	@Query("userById")
	@Description("Retrive an user given its id, if exists.")
	public User getUserById(@Name("id") String userId) {
		return userService.getUserById(Long.parseLong(userId));
	}
	
	@Query("")
	public List<String> getUsersPasswordList(@Source List<User> users){
		return users.stream().map(User::getUsername).collect(Collectors.toList());
	}
	
	@Mutation("addUser")
	@Description("Adds to the service the new user provided.")
	public User addUser(User user) {
		return userService.addNewUser(user);
	}
	
	@Mutation("updateUser")
	@Description("Updates the user with the one provided.")
	public User updateUser(User user, @Name("jwt") String token) {
		return userService.updateUser(user.getId(), user, authService.parseToken(token));
	}
	
	@Mutation("removeUser")
	@Description("Removes a user given his id.")
	public User removeUser(@Name("userId") Long userId, @Name("jwt") String token) {
		return userService.deleteUser(userId, authService.parseToken(token));
	}

}