package com.zszuri.winaball.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.zszuri.winaball.model.Territory;

public interface TerritoryRepository extends CrudRepository<Territory, Integer> {
	
	Optional<Territory> findByDescription(String description);

}
