package com.acme.estore.user.ws.io.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity(name="user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = -913091384710493677L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name="user_id", nullable=false, unique=true, updatable=false)
	private String userId;
	
	@Column(name="user_first_name", nullable=false, length=50)
	private String firstName;
	
	@Column(name="user_last_name", nullable=false, length=50)
	private String lastName;
	
	@Column(name="user_email", nullable=false, length=50, unique=true)
	private String email;
	
	@Column(name="user_password", nullable=false)
	private String encryptedPassword;
	
	private String emailVerificationToken;
	
	@Column(name="user_email_verification_status", nullable=false)
	private Boolean emailVerificationStatus = false;
	
	@OneToMany(mappedBy="userDto", cascade=CascadeType.ALL)
	private List<AddressEntity> address;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getEncryptedPassword() {
		return encryptedPassword;
	}


	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}


	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}


	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = "temporary string"; //emailVerificationToken;
	}


	public Boolean getEmailVerificationStatus() {
		return emailVerificationStatus;
	}


	public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public List<AddressEntity> getAddress() {
		return address;
	}


	public void setAddress(List<AddressEntity> address) {
		this.address = address;
	}
	
	

}
