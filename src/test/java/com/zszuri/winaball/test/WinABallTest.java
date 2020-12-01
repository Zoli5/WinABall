package com.zszuri.winaball.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zszuri.winaball.dao.RedeemedCouponRepository;
import com.zszuri.winaball.dao.UserRepository;
import com.zszuri.winaball.model.RedeemedCoupon;
import com.zszuri.winaball.model.Territory;
import com.zszuri.winaball.model.User;
import com.zszuri.winaball.requests.RedeemCouponRequest;
import com.zszuri.winaball.requests.UserRegistrationRequest;
import com.zszuri.winaball.responses.RedeemedCouponResponse;
import com.zszuri.winaball.responses.UserResponse;

public class WinABallTest extends AbstractTest {
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private RedeemedCouponRepository couponRepository;
	
	@Mock
	private User userMock;
	
	@Mock
	private Territory territoryMock;
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
		
	@Test
	public void getUserByEmailTest() throws Exception {
		
		User user = new User("email@test.com");
		user.setId(1);
		user.setName("test user");
		user.setPostCode(9023);
		user.setCity("Győr");
		user.setStreet("Szent István út");
		user.setCountry("Hungary");
		user.setDateOfBirth(ZonedDateTime.of(2000, 2, 15, 0, 0, 0, 0, ZoneId.systemDefault()));
		
		Mockito.when(userRepository.findByEmail("email@test.com")).thenReturn(Optional.of(user));
		
		String uri = "/winaball/rest/user/?email=email@test.com";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    UserResponse userResponse = super.mapFromJson(content, UserResponse.class);
	    assertEquals(userResponse.getName(), user.getName());     		
	}
	
	@Test
	public void userNotOldEnough() throws JsonProcessingException, Exception {
		
		UserRegistrationRequest request = new UserRegistrationRequest();
		request.setEmail("email@test.com");
		request.setName("test user");
		request.setPostCode(9023);
		request.setCity("Győr");
		request.setStreet("Szent István út");
		request.setCountry("Hungary");
		request.setDateOfBirth("2010-11-30");
		
		String uri = "/winaball/rest/user";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(super.mapToJson(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(400, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals("You must be over 13 to register.", content);    
	}
	
	@Test
	public void emailNotUnique() throws JsonProcessingException, Exception {
		Mockito.when(userRepository.findByEmail("email@test.com")).thenReturn(Optional.of(new User("email@test.com")));
		
		UserRegistrationRequest request = new UserRegistrationRequest();
		request.setEmail("email@test.com");
		request.setName("test user");
		request.setPostCode(9023);
		request.setCity("Győr");
		request.setStreet("Szent István út");
		request.setCountry("Hungary");
		request.setDateOfBirth("1990-11-30");
		
		String uri = "/winaball/rest/user";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(super.mapToJson(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(400, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals("The e-mail is already registred!", content);   
		
	}
	
	@Test
	public void successfullRegistration() throws JsonProcessingException, Exception {
				
		User user = new User("email@test.com");
		user.setId(1);
		user.setName("test user");
		user.setPostCode(9023);
		user.setCity("Győr");
		user.setStreet("Szent István út");
		user.setCountry("Hungary");
		user.setDateOfBirth(ZonedDateTime.of(1990, 11, 30, 0, 0, 0, 0, ZoneId.systemDefault()));
		
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		
		UserRegistrationRequest request = new UserRegistrationRequest();
		request.setEmail("email@test.com");
		request.setName("test user");
		request.setPostCode(9023);
		request.setCity("Győr");
		request.setStreet("Szent István út");
		request.setCountry("Hungary");
		request.setDateOfBirth("1990-11-30");
		
		String uri = "/winaball/rest/user";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(super.mapToJson(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    UserResponse userResponse = super.mapFromJson(content, UserResponse.class);
	    assertEquals(userResponse.getName(), request.getName());
		
	}
	
	@Test
	public void couponIncorrect() throws JsonProcessingException, Exception {
		
		RedeemCouponRequest request = new RedeemCouponRequest(1, "afhafjafjadfgafda", 1);
		
		String uri = "/winaball/rest/coupon";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(super.mapToJson(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(400, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals("The coupons is incorrect!", content);
		
	}
	
	@Test
	public void couponAlreadyUsed() throws JsonProcessingException, Exception {
		
		String coupon = "1234567890";
		
		Mockito.when(userMock.getEmail()).thenReturn("test@test.com");
		
		RedeemCouponRequest request = new RedeemCouponRequest(1, coupon, 1);
		
		RedeemedCoupon savedCoupon = new RedeemedCoupon();
		savedCoupon.setCoupon(coupon);
		savedCoupon.setId(1);
		savedCoupon.setTerritoryId(1);
		savedCoupon.setUserId(1);
		savedCoupon.setWinner(false);
		savedCoupon.setUser(userMock);
		savedCoupon.setTerritory(territoryMock);
		
		Mockito.when(couponRepository.findByCoupon(coupon)).thenReturn(Optional.of(savedCoupon));
		
		String uri = "/winaball/rest/coupon";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(super.mapToJson(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(400, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    assertEquals("The coupons is already used!", content);
		
	}
	
	@Test
	public void winnerCouponDailyLimitExceeded() throws JsonProcessingException, Exception {
		String coupon = "1234567890";
		
		// User
		User user = new User("email@test.com");
		user.setId(1);
		user.setName("test user");
		user.setPostCode(9023);
		user.setCity("Győr");
		user.setStreet("Szent István út");
		user.setCountry("Hungary");
		user.setDateOfBirth(ZonedDateTime.of(2000, 2, 15, 0, 0, 0, 0, ZoneId.systemDefault()));
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(userMock.getEmail()).thenReturn("test@test.com");
		
		// Do not find coupon	
		Mockito.when(couponRepository.findByCoupon(coupon)).thenReturn(Optional.empty());
		
		// Coupon repository
		Mockito.when(couponRepository.numberOfNotWinnerCoupons(1)).thenReturn(39);
		Mockito.when(couponRepository.numberOfTodaysWinnerCouponsByTerritory(1)).thenReturn(250);
		Mockito.when(couponRepository.numberOfWinnersByTerritory(1)).thenReturn(9000);
		
		// Coupon from DB
		RedeemedCoupon savedCoupon = new RedeemedCoupon();
		savedCoupon.setId(1);
		savedCoupon.setCreatedDateTime(ZonedDateTime.now());
		savedCoupon.setTerritoryId(1);
		savedCoupon.setTerritory(territoryMock);
		savedCoupon.setUserId(1);
		savedCoupon.setUser(userMock);
		savedCoupon.setWinner(false);
		savedCoupon.setCoupon(coupon);
		
		Mockito.when(couponRepository.save(Mockito.any(RedeemedCoupon.class))).thenReturn(savedCoupon);
		
		RedeemCouponRequest request = new RedeemCouponRequest(1, coupon, 1);
		String uri = "/winaball/rest/coupon";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(super.mapToJson(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    RedeemedCouponResponse couponResponse = super.mapFromJson(content, RedeemedCouponResponse.class);
	    assertEquals(false, couponResponse.isWinner());
	}
	
	@Test
	public void winnerCouponLimitExceeded() throws JsonProcessingException, Exception {
		String coupon = "1234567890";
		
		// User
		User user = new User("email@test.com");
		user.setId(1);
		user.setName("test user");
		user.setPostCode(9023);
		user.setCity("Győr");
		user.setStreet("Szent István út");
		user.setCountry("Hungary");
		user.setDateOfBirth(ZonedDateTime.of(2000, 2, 15, 0, 0, 0, 0, ZoneId.systemDefault()));
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(userMock.getEmail()).thenReturn("test@test.com");
		
		// Do not find coupon	
		Mockito.when(couponRepository.findByCoupon(coupon)).thenReturn(Optional.empty());
		
		// Coupon repository
		Mockito.when(couponRepository.numberOfNotWinnerCoupons(1)).thenReturn(39);
		Mockito.when(couponRepository.numberOfTodaysWinnerCouponsByTerritory(1)).thenReturn(249);
		Mockito.when(couponRepository.numberOfWinnersByTerritory(1)).thenReturn(10000);
		
		// Coupon from DB
		RedeemedCoupon savedCoupon = new RedeemedCoupon();
		savedCoupon.setId(1);
		savedCoupon.setCreatedDateTime(ZonedDateTime.now());
		savedCoupon.setTerritoryId(1);
		savedCoupon.setTerritory(territoryMock);
		savedCoupon.setUserId(1);
		savedCoupon.setUser(userMock);
		savedCoupon.setWinner(false);
		savedCoupon.setCoupon(coupon);
		
		Mockito.when(couponRepository.save(Mockito.any(RedeemedCoupon.class))).thenReturn(savedCoupon);
		
		RedeemCouponRequest request = new RedeemCouponRequest(1, coupon, 1);
		String uri = "/winaball/rest/coupon";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(super.mapToJson(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    RedeemedCouponResponse couponResponse = super.mapFromJson(content, RedeemedCouponResponse.class);
	    assertEquals(false, couponResponse.isWinner());
		
	}
	
	@Test
	public void notWinnerCoupon() throws JsonProcessingException, Exception {
		String coupon = "1234567890";
		
		// User
		User user = new User("email@test.com");
		user.setId(1);
		user.setName("test user");
		user.setPostCode(9023);
		user.setCity("Győr");
		user.setStreet("Szent István út");
		user.setCountry("Hungary");
		user.setDateOfBirth(ZonedDateTime.of(2000, 2, 15, 0, 0, 0, 0, ZoneId.systemDefault()));
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(userMock.getEmail()).thenReturn("test@test.com");
		
		// Do not find coupon	
		Mockito.when(couponRepository.findByCoupon(coupon)).thenReturn(Optional.empty());
		
		// Coupon repository
		Mockito.when(couponRepository.numberOfNotWinnerCoupons(1)).thenReturn(20);
		Mockito.when(couponRepository.numberOfTodaysWinnerCouponsByTerritory(1)).thenReturn(240);
		Mockito.when(couponRepository.numberOfWinnersByTerritory(1)).thenReturn(9000);
		
		// Coupon from DB
		RedeemedCoupon savedCoupon = new RedeemedCoupon();
		savedCoupon.setId(1);
		savedCoupon.setCreatedDateTime(ZonedDateTime.now());
		savedCoupon.setTerritoryId(1);
		savedCoupon.setTerritory(territoryMock);
		savedCoupon.setUserId(1);
		savedCoupon.setUser(userMock);
		savedCoupon.setWinner(false);
		savedCoupon.setCoupon(coupon);
		
		Mockito.when(couponRepository.save(Mockito.any(RedeemedCoupon.class))).thenReturn(savedCoupon);
		
		RedeemCouponRequest request = new RedeemCouponRequest(1, coupon, 1);
		String uri = "/winaball/rest/coupon";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(super.mapToJson(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    RedeemedCouponResponse couponResponse = super.mapFromJson(content, RedeemedCouponResponse.class);
	    assertEquals(false, couponResponse.isWinner());
		
	}
	
	@Test
	public void copounWinner() throws JsonProcessingException, Exception {
		String coupon = "1234567890";
		
		// User
		User user = new User("email@test.com");
		user.setId(1);
		user.setName("test user");
		user.setPostCode(9023);
		user.setCity("Győr");
		user.setStreet("Szent István út");
		user.setCountry("Hungary");
		user.setDateOfBirth(ZonedDateTime.of(2000, 2, 15, 0, 0, 0, 0, ZoneId.systemDefault()));
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(userMock.getEmail()).thenReturn("test@test.com");
		
		// Do not find coupon	
		Mockito.when(couponRepository.findByCoupon(coupon)).thenReturn(Optional.empty());
		
		// Coupon repository
		Mockito.when(couponRepository.numberOfNotWinnerCoupons(1)).thenReturn(39);
		Mockito.when(couponRepository.numberOfTodaysWinnerCouponsByTerritory(1)).thenReturn(240);
		Mockito.when(couponRepository.numberOfWinnersByTerritory(1)).thenReturn(9000);
		
		// Coupon from DB
		RedeemedCoupon savedCoupon = new RedeemedCoupon();
		savedCoupon.setId(1);
		savedCoupon.setCreatedDateTime(ZonedDateTime.now());
		savedCoupon.setTerritoryId(1);
		savedCoupon.setTerritory(territoryMock);
		savedCoupon.setUserId(1);
		savedCoupon.setUser(userMock);
		savedCoupon.setWinner(false);
		savedCoupon.setCoupon(coupon);
		
		Mockito.when(couponRepository.save(Mockito.any(RedeemedCoupon.class))).thenReturn(savedCoupon);
		
		RedeemCouponRequest request = new RedeemCouponRequest(1, coupon, 1);
		String uri = "/winaball/rest/coupon";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(super.mapToJson(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
	    assertEquals(200, status);
	    String content = mvcResult.getResponse().getContentAsString();
	    RedeemedCouponResponse couponResponse = super.mapFromJson(content, RedeemedCouponResponse.class);
	    assertEquals(true, couponResponse.isWinner());
	}
}
