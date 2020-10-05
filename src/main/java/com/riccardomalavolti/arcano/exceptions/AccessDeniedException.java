package com.riccardomalavolti.arcano.exceptions;

public class AccessDeniedException extends RuntimeException {

   private static final long serialVersionUID = 8843383785543220772L;

	public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
