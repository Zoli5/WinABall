package com.zszuri.winaball.responses;

import java.time.ZonedDateTime;

public class UserResponse {
	int id;
	String name;
	String email;
	int postCode;
	String city;
	String street;
	String country;
	ZonedDateTime dateOfBirth;
	
	public UserResponse() {
	}
	
	public UserResponse(int id, String name, String email, int postCode, String city, String street, String country,
			ZonedDateTime dateOfBirth) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.postCode = postCode;
		this.city = city;
		this.street = street;
		this.country = country;
		this.dateOfBirth = dateOfBirth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPostCode() {
		return postCode;
	}

	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public ZonedDateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(ZonedDateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
