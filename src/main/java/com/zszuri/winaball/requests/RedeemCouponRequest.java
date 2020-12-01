package com.zszuri.winaball.requests;

public class RedeemCouponRequest {
	int userId;
	String coupon;
	int territoryId;
	
	public RedeemCouponRequest(int userId, String coupon, int territoryId) {
		this.userId = userId;
		this.coupon = coupon;
		this.territoryId = territoryId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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
	
	
}
