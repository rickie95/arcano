package com.riccardomalavolti.arcano.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {
	
	private static final String SALT = BCrypt.gensalt();
	
	private PasswordHash() {};

	public static String hash(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, SALT);
	}
	
	public static boolean checkPassword(String toBeVerified, String hashed) {
		return BCrypt.checkpw(toBeVerified, hashed);
	}

}
