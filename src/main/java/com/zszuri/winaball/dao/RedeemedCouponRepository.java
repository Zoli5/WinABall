package com.zszuri.winaball.dao;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.zszuri.winaball.model.RedeemedCoupon;

public interface RedeemedCouponRepository extends CrudRepository<RedeemedCoupon, Integer>, PagingAndSortingRepository<RedeemedCoupon, Integer> {

	Optional<RedeemedCoupon> findByCoupon(String coupon);
	
	@Query(value = "select count(rc.id) from redeemedcoupons rc where rc.id > (select max(rd.id) from redeemedcoupons rd where rd.territoryid = :territoryid and rd.iswinner = true)", nativeQuery = true)
	int numberOfNotWinnerCoupons(@Param("territoryid") int territoryId);    
	
	List<RedeemedCoupon> findByIsWinner(boolean winner);
	
	@Query(value = "select count(id) from redeemedcoupons where territoryid = :territoryid AND iswinner = true AND DATE(createddatetime) >= CURRENT_DATE AND DATE(createddatetime) < CURRENT_DATE + INTERVAL '1 DAY'", nativeQuery = true)
	int numberOfTodaysWinnerCouponsByTerritory(@Param("territoryid") int territoryId);
	
	@Query(value = "select count(id) from redeemedcoupons where territoryid = :territoryid AND iswinner = true", nativeQuery = true)
	int numberOfWinnersByTerritory(@Param("territoryid") int territoryId);
	
	Page<RedeemedCoupon> findAllByIsWinner(boolean winner, Pageable page);
	
	Page<RedeemedCoupon> findAllByIsWinnerAndCreatedDateTimeBetween(boolean winner, ZonedDateTime fromDateTime, ZonedDateTime toDateTime, Pageable page);
}
