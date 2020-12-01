package com.zszuri.winaball.service;

import java.time.ZonedDateTime;
import java.util.List;

import com.zszuri.winaball.requests.RedeemCouponRequest;
import com.zszuri.winaball.requests.UserRegistrationRequest;
import com.zszuri.winaball.responses.RedeemedCouponResponse;
import com.zszuri.winaball.responses.TerritoryResponse;
import com.zszuri.winaball.responses.UserResponse;
import com.zszuri.winaball.responses.WinnersPageableResponse;

public interface WinABallService {
	
	List<TerritoryResponse> getAllTerritories();
	
	UserResponse getUserByEmail(String email);
	
	UserResponse registerUser(UserRegistrationRequest request);
	
	RedeemedCouponResponse getCoupon(String coupon);
	
	RedeemedCouponResponse redeemCoupon(RedeemCouponRequest request);
	
	WinnersPageableResponse loadWinners(Integer pageNumber, Integer pageSize);
	
	WinnersPageableResponse loadFilteredWinners(ZonedDateTime fromDate, ZonedDateTime toDate, Integer pageNumber, Integer pageSize);

}
