package com.zszuri.winaball.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.zszuri.winaball.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	Optional<User> findByEmail(String email);

}
