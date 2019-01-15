package com.checkpointformcleaner.repository;

import org.springframework.data.repository.CrudRepository;

import com.checkpointformcleaner.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	public User findByEmail(String email);

	public User findByActivation(String code);

}
