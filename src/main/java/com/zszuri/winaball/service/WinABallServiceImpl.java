package com.zszuri.winaball.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zszuri.winaball.dao.RedeemedCouponRepository;
import com.zszuri.winaball.dao.TerritoryRepository;
import com.zszuri.winaball.dao.UserRepository;
import com.zszuri.winaball.model.RedeemedCoupon;
import com.zszuri.winaball.model.Territory;
import com.zszuri.winaball.model.User;
import com.zszuri.winaball.requests.RedeemCouponRequest;
import com.zszuri.winaball.requests.UserRegistrationRequest;
import com.zszuri.winaball.responses.RedeemedCouponResponse;
import com.zszuri.winaball.responses.TerritoryResponse;
import com.zszuri.winaball.responses.UserResponse;
import com.zszuri.winaball.responses.WinnerResponse;
import com.zszuri.winaball.responses.WinnersPageableResponse;

@Service
public class WinABallServiceImpl implements WinABallService {
	
	private TerritoryRepository territoryRepository;	
	private RedeemedCouponRepository redeemedCouponRepository;
	private UserRepository userRepository;
	
	@Autowired
	public WinABallServiceImpl(TerritoryRepository territoryRepository, RedeemedCouponRepository redeemedCouponRepository, UserRepository userRepository) {
		this.territoryRepository = territoryRepository;
		this.redeemedCouponRepository = redeemedCouponRepository;
		this.userRepository = userRepository;
	}

	@Override
	public UserResponse getUserByEmail(String email) {
		Optional<User> userFromDB = userRepository.findByEmail(email);
		
		if (userFromDB.isEmpty()) {
			return null;
		} else {
			User user = userFromDB.get();
			return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPostCode(), user.getCity(), user.getStreet(), user.getCountry(), user.getDateOfBirth());
		}	
	}
	
	@Override
	public RedeemedCouponResponse redeemCoupon(RedeemCouponRequest request) {
		User user = userRepository.findById(request.getUserId()).get();
		Territory territory = territoryRepository.findById(Integer.valueOf(request.getTerritoryId())).get();

		boolean isWinner = calculateWinner(territory);		
		
		RedeemedCoupon couponToRedeem = new RedeemedCoupon();
		couponToRedeem.setUser(user);
		couponToRedeem.setCoupon(request.getCoupon());
		couponToRedeem.setTerritory(territory);
		couponToRedeem.setWinner(isWinner);	
		couponToRedeem.setCreatedDateTime(ZonedDateTime.now());
		
		RedeemedCoupon savedCoupon = redeemedCouponRepository.save(couponToRedeem);
		return new RedeemedCouponResponse(savedCoupon.getUser().getEmail(), savedCoupon.getCoupon(), savedCoupon.getTerritory().getDescription(), isWinner);
	}
	
	@Override
	public List<TerritoryResponse> getAllTerritories() {
		Iterable<Territory> territories = territoryRepository.findAll();
		
		List<TerritoryResponse> responses = new ArrayList<>();
		
		territories.forEach(territory -> {
			responses.add(new TerritoryResponse(territory.getId(), territory.getDescription()));
		});
		
		return responses;
	}

	@Override
	public WinnersPageableResponse loadWinners(Integer pageNumber, Integer pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<RedeemedCoupon> winnerPage = redeemedCouponRepository.findAllByIsWinner(true, page);
		
		List<WinnerResponse> winnerResponses = new ArrayList<WinnerResponse>();
		winnerPage.forEach(a -> winnerResponses.add(new WinnerResponse(a.getCoupon(), a.getUser().getEmail())));
		
		return new WinnersPageableResponse(pageNumber, pageSize, winnerPage.getTotalPages(), winnerResponses);
	}

	@Override
	public WinnersPageableResponse loadFilteredWinners(ZonedDateTime fromDate, ZonedDateTime toDate,
			Integer pageNumber, Integer pageSize) {

		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<RedeemedCoupon> winnerPage = redeemedCouponRepository.findAllByIsWinnerAndCreatedDateTimeBetween(true, fromDate, toDate, page);
		
		List<WinnerResponse> winnerResponses = new ArrayList<WinnerResponse>();
		winnerPage.forEach(a -> winnerResponses.add(new WinnerResponse(a.getCoupon(), a.getUser().getEmail())));
		
		return new WinnersPageableResponse(pageNumber, pageSize, winnerPage.getTotalPages(), winnerResponses);
	}

	@Override
	public RedeemedCouponResponse getCoupon(String coupon) {
		Optional<RedeemedCoupon> optionalCoupon = redeemedCouponRepository.findByCoupon(coupon);
		
		return optionalCoupon.isEmpty()? null : new RedeemedCouponResponse(optionalCoupon.get().getUser().getEmail(), optionalCoupon.get().getCoupon(), optionalCoupon.get().getTerritory().getDescription(), optionalCoupon.get().isWinner());
	}

	@Override
	public UserResponse registerUser(UserRegistrationRequest request) {	
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(request.getDateOfBirth(), formatter);
		ZonedDateTime zdBirthDate = date.atStartOfDay(ZoneId.systemDefault());
		
		user.setDateOfBirth(zdBirthDate);
		user.setCountry(request.getCountry());
		user.setPostCode(request.getPostCode());
		user.setCity(request.getCity());
		user.setStreet(request.getStreet());
		
		User savedUser = userRepository.save(user);
		
		return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPostCode(), savedUser.getCity(), savedUser.getStreet(), savedUser.getCountry(), savedUser.getDateOfBirth());
	}
	
	private boolean calculateWinner(Territory territory) {	
		int numberOfNotWinnerCouponsSinceLastWinner = redeemedCouponRepository.numberOfNotWinnerCoupons(territory.getId());
		int numberOfTodaysWinnerCoupons = redeemedCouponRepository.numberOfTodaysWinnerCouponsByTerritory(territory.getId());
		int numberOfWinners = redeemedCouponRepository.numberOfWinnersByTerritory(territory.getId());
		
		if (numberOfTodaysWinnerCoupons >= territory.getMaxDailyWinners()) {
			return false;
		} else if (numberOfWinners >= territory.getMaxWinners()) {
			return false;
		} else if (numberOfNotWinnerCouponsSinceLastWinner == (territory.getWinnerFrequency()-1)) {
			return true;
		}
		return false;
	}
}
