package com.acme.estore.user.ws.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "address")
public class AddressEntity implements Serializable {
	private static final long serialVersionUID = 869528766806024615L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "address_id", length = 30, nullable = false)
	private String addressId;

	@Column(name = "address_city", length = 30, nullable = false)
	private String city;

	@Column(name = "address_country", length = 15, nullable = false)
	private String country;

	@Column(name = "address_street", length = 15, nullable = false)
	private String street;

	@Column(name = "address_zipcode", length = 7, nullable = false)
	private String zipCode;

	@Column(name = "address_type", length = 10, nullable = false)
	private String type;

	@ManyToOne
	@JoinColumn(name="user_entity_id")
	private UserEntity userDto;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UserEntity getUserDto() {
		return userDto;
	}

	public void setUserDto(UserEntity userDto) {
		this.userDto = userDto;
	}
	
}
