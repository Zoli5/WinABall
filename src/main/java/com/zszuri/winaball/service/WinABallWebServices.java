package com.zszuri.winaball.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zszuri.winaball.requests.RedeemCouponRequest;
import com.zszuri.winaball.requests.UserRegistrationRequest;
import com.zszuri.winaball.responses.RedeemedCouponResponse;
import com.zszuri.winaball.responses.TerritoryResponse;
import com.zszuri.winaball.responses.UserResponse;
import com.zszuri.winaball.responses.WinnersPageableResponse;

@RestController
public class WinABallWebServices {
	
	private static final Long MIN_AGE = 13L;
	private static final int COUPON_LENGTH = 10;
	
	private static final String PREFIX = "/winaball/rest";
	private static final String GET_WINNERS = PREFIX + "/winners";
	private static final String GET_USER = PREFIX + "/user";
	private static final String GET_ALL_TERRITORIES = PREFIX + "/territories";
	private static final String POST_COUPON = PREFIX + "/coupon";
	private static final String POST_USER = PREFIX + "/user";
	
	private WinABallService winABallService;

	@Autowired
	public WinABallWebServices(WinABallService winABallService) {
		this.winABallService = winABallService;
	}
		
	@GetMapping(GET_USER)
	public ResponseEntity<UserResponse> getUserByEmail (@RequestParam("email") String email) {
		return new ResponseEntity<UserResponse>(winABallService.getUserByEmail(email), HttpStatus.OK);
	}
	
	@GetMapping(GET_ALL_TERRITORIES)
	public ResponseEntity<List<TerritoryResponse>> getTerritories() {
		return new ResponseEntity<List<TerritoryResponse>>(winABallService.getAllTerritories(), HttpStatus.OK);
	}
	
	@GetMapping(GET_WINNERS)
	public ResponseEntity<WinnersPageableResponse> listWinners (
			@RequestParam(value = "fromDate", required = false) String fromDate, 
			@RequestParam(value = "toDate", required = false) String toDate, 
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNumber,
			@RequestParam(value = "page", required = false, defaultValue = "30") Integer pageSize) {
		if (fromDate == null || toDate == null) {
			return new ResponseEntity<WinnersPageableResponse>(winABallService.loadWinners(pageNumber, pageSize), HttpStatus.OK);
		}
				
		return new ResponseEntity<WinnersPageableResponse>(winABallService.loadFilteredWinners(convertStringToDate(fromDate), convertStringToDate(toDate), pageNumber, pageSize), HttpStatus.OK);
	}
	
	@PostMapping(POST_COUPON)
	public ResponseEntity<? extends Object> redeemCoupon(@RequestBody RedeemCouponRequest request) {
		if (request.getCoupon().length() != COUPON_LENGTH) {
			return new ResponseEntity<String>(new String("The coupons is incorrect!"), HttpStatus.BAD_REQUEST);
		}
		
		
		RedeemedCouponResponse coupon = winABallService.getCoupon(request.getCoupon());
		if (coupon != null) {
			return new ResponseEntity<String>(new String("The coupons is already used!"), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<RedeemedCouponResponse>(winABallService.redeemCoupon(request), HttpStatus.OK);	
	}
	
	@PostMapping(POST_USER)
	public ResponseEntity<? extends Object> registerUser(@RequestBody UserRegistrationRequest request) {
		if (convertStringToDate(request.getDateOfBirth()).isAfter(ZonedDateTime.now().minus(MIN_AGE, ChronoUnit.YEARS))) {
			return new ResponseEntity<String>(new String("You must be over 13 to register."), HttpStatus.BAD_REQUEST);
		}
		
		UserResponse savedUser = winABallService.getUserByEmail(request.getEmail());
		
		if (savedUser != null) {
			return new ResponseEntity<String>(new String("The e-mail is already registred!"), HttpStatus.BAD_REQUEST);
		}
				
		return new ResponseEntity<UserResponse>(winABallService.registerUser(request), HttpStatus.OK);
	}
	
	private ZonedDateTime convertStringToDate(String date) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(date, formatter);
			ZonedDateTime zonedDate = localDate.atStartOfDay(ZoneId.systemDefault());
			return zonedDate;
		} catch (DateTimeException e) {
			throw new IllegalArgumentException(date + " is not in valid format! Use yyyy-MM-dd date format.");
		}
	}	
}
