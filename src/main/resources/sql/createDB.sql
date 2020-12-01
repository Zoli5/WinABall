-- create database using the following command
-- CREATE DATABASE winaball;

-- psql -d winaball -U postgres -f .\createDB.sql

-- disconnect from psql and connect to the newly created database and run the following script:
CREATE TABLE Users (
	id SERIAL PRIMARY KEY,
	name varchar(50),
	email varchar(40) UNIQUE NOT NULL,
	postcode integer,
	city varchar (50),
	street varchar (50),
	country varchar (50),
	dob date
);

CREATE TABLE Territories (
	id SERIAL PRIMARY KEY,
	description varchar(30) NOT NULL,
	winnerfrequency integer NOT NULL,
	maxwinners integer NOT NULL,
	maxdailywinners integer NOT NULL
);

CREATE TABLE RedeemedCoupons (
	id SERIAL PRIMARY KEY,
	userid integer NOT NULL,
	coupon varchar(10) UNIQUE NOT NULL,
	territoryid integer NOT NULL,
	iswinner boolean NOT NULL,
	createddatetime timestamp,
    CONSTRAINT fk_redeemedcoupons_territoryid
      FOREIGN KEY(territoryid) 
	  REFERENCES territories(id),
	CONSTRAINT fk_redeemedcoupons_userid
      FOREIGN KEY(userid) 
	  REFERENCES users(id)
);

INSERT INTO Territories(description, winnerfrequency, maxwinners, maxdailywinners) VALUES ('Germany', 40, 10000, 250);
INSERT INTO Territories(description, winnerfrequency, maxwinners, maxdailywinners) VALUES ('Hungary', 80, 5000, 100);