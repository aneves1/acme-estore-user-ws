package com.acme.estore.user.ws.dto;

import java.util.List;

public class UserDTO {
	private String userId;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String emailVerificationToken;
	private Boolean emailVerificatinStatus = false;
	private List<AddressDTO> address;

	public UserDTO() {

	}

	public UserDTO(String userId, String firstName, String lastName, String email) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}

	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}

	public Boolean getEmailVerificatinStatus() {
		return emailVerificatinStatus;
	}

	public void setEmailVerificatinStatus(Boolean emailVerificatinStatus) {
		this.emailVerificatinStatus = emailVerificatinStatus;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<AddressDTO> getAddress() {
		return address;
	}

	public void setAddress(List<AddressDTO> address) {
		this.address = address;
	}

}
