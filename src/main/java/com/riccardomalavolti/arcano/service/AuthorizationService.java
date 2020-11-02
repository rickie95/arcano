package com.riccardomalavolti.arcano.service;

import javax.inject.Inject;

import com.riccardomalavolti.arcano.exceptions.AccessDeniedException;
import com.riccardomalavolti.arcano.model.Ownable;
import com.riccardomalavolti.arcano.model.User;

public class AuthorizationService {
	
	@Inject
	UserService userService;

	public void verifyOwnershipOf(Ownable resource, final String requesterUsername) {
		User requester = userService.getUserByUsername(requesterUsername);
		if(!resource.isOwnedBy(requester))
			throw new AccessDeniedException("You don't own this resource.");
	}
}
