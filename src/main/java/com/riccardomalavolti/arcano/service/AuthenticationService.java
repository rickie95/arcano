package com.riccardomalavolti.arcano.service;

import java.security.Key;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

import com.riccardomalavolti.arcano.model.User;
import com.riccardomalavolti.arcano.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@ApplicationScoped
public class AuthenticationService {
	
	public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	@Inject
	UserRepository userRepository;

	public String authenticateUser(User user) {
		// Authenticate the user using the credentials provided
		authenticate(user);

		// Issue a token for the user
		return issueToken(user.getUsername());
	}
	
	private void authenticate(User user) {
		if (user.getUsername() == null || user.getPassword() == null)
			throw new IllegalArgumentException("Username or passowrd are empty.");
		
		userRepository.authenticate(user);
	}
	
	private String issueToken(String username) {
		return Jwts.builder().setSubject(username).signWith(key).compact();
	}
	
	public boolean isTokenValid(String token) {
		try {
		    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		    return true;
		} catch (SecurityException e) {
			return false;
		} catch (JwtException exception) {
			System.out.println("An error has occured while parsing a JWT token");
			return false;
		}
	}

	public String refreshToken(String tokenDetails) {
		return tokenDetails;
	}

	/*
	 * Parse a JWT and extracts the username of the authenticated user.
	 * 
	 * @param authenticationToken
	 * @return a String with the authenticated User.
	 */
	public String parseToken(String authenticationToken) {
		Claims claims;
		try {
			 claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authenticationToken).getBody();
		} catch (JwtException ex){
			throw new NotAuthorizedException("Your token is either invalid or expired");
		}
		return claims.getSubject();
	}
}
