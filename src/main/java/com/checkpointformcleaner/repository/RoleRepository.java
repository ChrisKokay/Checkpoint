package com.checkpointformcleaner.repository;

import org.springframework.data.repository.CrudRepository;

import com.checkpointformcleaner.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	public Role findByRole(String role);
	
}
