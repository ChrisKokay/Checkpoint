package com.checkpointformcleaner.service;

import com.checkpointformcleaner.entity.User;

public interface UserService {
	
	public User findByEmail(String email);

	public String registerUser(User user);

	public String userActivation(String code);

}
