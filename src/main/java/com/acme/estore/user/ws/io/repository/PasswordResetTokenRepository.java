package com.acme.estore.user.ws.io.repository;

import org.springframework.data.repository.CrudRepository;

import com.acme.estore.user.ws.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

}
