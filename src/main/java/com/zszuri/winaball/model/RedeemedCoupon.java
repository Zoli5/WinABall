package com.zszuri.winaball.model;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="REDEEMEDCOUPONS")
public class RedeemedCoupon {

	@Id
    @SequenceGenerator(name = "REDEEMEDCOUPONS_ID_SEQ", sequenceName = "REDEEMEDCOUPONS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REDEEMEDCOUPONS_ID_SEQ")
	@Column(name="id")
	int id;
	
	@Column(name="coupon", nullable = false)
	String coupon;
	
    @Column(name = "TERRITORYID", updatable = false, insertable = false, nullable = false)
    private int territoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERRITORYID")
    private Territory territory;
    
    @Column(name = "USERID", updatable = false, insertable = false, nullable = false)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERID")
    private User user;
	
	@Column(name="ISWINNER", nullable = false)
	boolean isWinner;
	
	@Column(name="createddatetime")
	ZonedDateTime createdDateTime;
	
	public RedeemedCoupon() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public int getTerritoryId() {
		return territoryId;
	}

	public void setTerritoryId(int territoryId) {
		this.territoryId = territoryId;
	}

	public Territory getTerritory() {
		return territory;
	}

	public void setTerritory(Territory territory) {
		this.territory = territory;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isWinner() {
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	public ZonedDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(ZonedDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
}
