package com.acme.estore.user.ws.model;

public enum ErrorMessage {
	
	MISSING_REQUIRED_FIELD("Missing required field."),
	RECORD_ALREADY_EXIST("Record already exists."),
	INTERNAL_SERVER_ERROR("Internal server error."),
	NO_RECORD_FOUND("Record was not found with provided user ID."),
	AUTHENTICATION_FALIED("Authentication failed."),
	COULD_NOT_UPDATE_RECORD("Could not update record."),
	COULD_NOT_DELETE_RECORD("Could not delete record."),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified.");
	
	private String errorMessage;
	
	ErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
