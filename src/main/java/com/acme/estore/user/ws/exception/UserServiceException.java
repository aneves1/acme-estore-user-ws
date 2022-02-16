package com.acme.estore.user.ws.exception;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = -1505582911032816357L;
	
	public UserServiceException(String message) {
		super(message);
	}

}
