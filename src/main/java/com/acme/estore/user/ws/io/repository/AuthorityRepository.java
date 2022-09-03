package com.acme.estore.user.ws.io.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acme.estore.user.ws.io.entity.AuthorityEntity;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

	AuthorityEntity findByAuthorityName(String name);
}
