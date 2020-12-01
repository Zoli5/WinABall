package com.zszuri.winaball.responses;

public class WinnerResponse {
	
	String coupon;
	String email;
	
	public WinnerResponse(String coupon, String email) {
		super();
		this.coupon = coupon;
		this.email = email;
	}
	
	public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
}
