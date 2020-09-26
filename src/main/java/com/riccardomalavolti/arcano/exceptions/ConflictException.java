package com.riccardomalavolti.arcano.exceptions;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

public class ConflictException extends ClientErrorException {

	private static final long serialVersionUID = 6679786088672936532L;

	public ConflictException() {
		super(Response.Status.CONFLICT);
	}
	
	public ConflictException(String message) {
		super(message, Response.Status.CONFLICT);
	}

}
