package com.acme.estore.user.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.estore.user.ws.dto.AddressDTO;
import com.acme.estore.user.ws.io.entity.AddressEntity;
import com.acme.estore.user.ws.io.entity.UserEntity;
import com.acme.estore.user.ws.io.repository.AddressRepository;
import com.acme.estore.user.ws.io.repository.UserRepository;
import com.acme.estore.user.ws.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDTO> getAddress(String usersId) {
		List<AddressDTO> result = new ArrayList<>();
		
		UserEntity userEntity = userRepository.findByUserId(usersId);
		if (userEntity == null) return result;
		
		List<AddressEntity> addresses = addressRepository.findAllByUserDto(userEntity);
		
		for (AddressEntity address : addresses) {
			result.add(new ModelMapper().map(address, AddressDTO.class));
		}
		
		
		return result;
	}

}
