package com.zszuri.winaball.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TERRITORIES")
public class Territory {

	@Id
    @SequenceGenerator(name = "TERRITORIES_ID_SEQ", sequenceName = "TERRITORIES_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TERRITORIES_ID_SEQ")
	@Column(name="id")
	int id;
	
	@Column(name="description", nullable = false)
	String description;
	
	@Column(name="winnerfrequency", nullable = false)
	int winnerFrequency;
	
	@Column(name="maxwinners", nullable = false)
	int maxWinners;
	
	@Column(name="maxdailywinners", nullable = false)
	int maxDailyWinners;
	
	public Territory() {
		super();
	}

	public Territory(String description, int winnerFrequency, int maxWinners, int maxDailyWinners) {
		super();
		this.description = description;
		this.winnerFrequency = winnerFrequency;
		this.maxWinners = maxWinners;
		this.maxDailyWinners = maxDailyWinners;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getWinnerFrequency() {
		return winnerFrequency;
	}

	public void setWinnerFrequency(int winnerFrequency) {
		this.winnerFrequency = winnerFrequency;
	}

	public int getMaxWinners() {
		return maxWinners;
	}

	public void setMaxWinners(int maxWinners) {
		this.maxWinners = maxWinners;
	}

	public int getMaxDailyWinners() {
		return maxDailyWinners;
	}

	public void setMaxDailyWinners(int maxDailyWinners) {
		this.maxDailyWinners = maxDailyWinners;
	}
}
