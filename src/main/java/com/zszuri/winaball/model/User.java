package com.zszuri.winaball.model;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
public class User {
	
	@Id
    @SequenceGenerator(name = "USERS_ID_SEQ", sequenceName = "USERS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_ID_SEQ")
	@Column(name="id")
	int id;
	
	@Column(name="name", nullable = false)
	String name;
	
	@Column(name="email", nullable = false)
	String email;
	
	@Column(name="postcode")
	int postCode;
	
	@Column(name="city")
	String city;
	
	@Column(name="street")
	String street;
	
	@Column(name="country")
	String country;
	
	@Column(name="dob", nullable = false)
	ZonedDateTime dateOfBirth;
	
	public User() {
	}

	public User(String email) {
		super();
		this.email = email;
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

	public ZonedDateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(ZonedDateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}	
}
