package com.zszuri.winaball.responses;

public class RedeemedCouponResponse {
	String email;
	String coupon;
	String territory;
	boolean winner;
	
	public RedeemedCouponResponse() {

	}

	public RedeemedCouponResponse(String email, String coupon, String territory, boolean winner) {
		super();
		this.email = email;
		this.coupon = coupon;
		this.territory = territory;
		this.winner = winner;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}
}
