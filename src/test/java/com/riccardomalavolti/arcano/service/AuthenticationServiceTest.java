package com.riccardomalavolti.arcano.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

import java.util.Optional;

import javax.ws.rs.NotAuthorizedException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.UserRepository;
import com.riccardomalavolti.arcano.security.PasswordHash;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class AuthenticationServiceTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	AuthenticationService authService;
	
	@Test
	void authenticateUserShouldThrowIllegalArgumentExceptionIfUsernameOrPasswordAreNull(){
		User userWithoutPassword = new User();
		userWithoutPassword.setUsername("Mike");
		
		assertThrows(IllegalArgumentException.class, () -> authService.authenticateUser(userWithoutPassword));
		
		User userWithoutUsername = new User();
		userWithoutUsername.setPassword("Secret");
		
		assertThrows(IllegalArgumentException.class, () -> authService.authenticateUser(userWithoutUsername));
	}
	
	@Test
	void authenticateUserShouldThrowNoAuthorizedExceptionIfUserIsNotFound() {
		User user = new User("Mike", "secret");
		
		when(userRepository.getUserByUsername(user.getUsername())).thenReturn(Optional.empty());
		
		assertThrows(NotAuthorizedException.class, () -> authService.authenticateUser(user));
	}
	
	@Test
	void authenticateUserShouldThrowNotAuthorizedExceptionWhenPasswordIsWrong() {
		User candidateUser = new User("Mike", "secret");
		User returnedUser = new User("Mike", PasswordHash.hash("differentSecret"));
		
		when(userRepository.getUserByUsername(candidateUser.getUsername()))
			.thenReturn(Optional.of(returnedUser));
		
		assertThrows(NotAuthorizedException.class, () -> authService.authenticateUser(candidateUser));
	}
	
	@Test
	void authenticateUserShouldConcludeWithoutErrorsIfCredentialsAreCorrect() {
		User candidateUser = new User("Mike", "secret");
		User returnedUser = new User("Mike", PasswordHash.hash("secret"));
		
		when(userRepository.getUserByUsername(candidateUser.getUsername()))
			.thenReturn(Optional.of(returnedUser));		
		
		 try (MockedStatic<PasswordHash> theMock = Mockito.mockStatic(PasswordHash.class)) {
			 theMock.when(() -> PasswordHash
					 .checkPassword(candidateUser.getPassword(), returnedUser.getPassword()))
			 .thenReturn(true);
			 
			 authService.authenticateUser(candidateUser);
			 theMock.verify(() -> PasswordHash
					 .checkPassword(eq(candidateUser.getPassword()), eq(returnedUser.getPassword())));
		}
	}

}