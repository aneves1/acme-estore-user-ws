package com.acme.estore.user.ws.io.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acme.estore.user.ws.io.entity.AddressEntity;
import com.acme.estore.user.ws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
	List<AddressEntity> findAllByUserDto(UserEntity userDto);
	AddressEntity findByAddressId(String addressId);
}
