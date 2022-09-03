package com.acme.estore.user.ws.io.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acme.estore.user.ws.io.entity.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
	
	RoleEntity findByRoleName(String name);

}
