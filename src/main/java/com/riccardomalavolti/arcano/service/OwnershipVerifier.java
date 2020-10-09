package com.riccardomalavolti.arcano.service;

public interface OwnershipVerifier {
	
	public void isUserOwnerOfResource(String userUsername, Long resourceId);

}
