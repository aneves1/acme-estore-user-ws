package com.acme.estore.user.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
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
	public List<AddressDTO> getAddresses(String usersId) {
		List<AddressDTO> result = new ArrayList<>();
		
		UserEntity userEntity = userRepository.findByUserId(usersId);
		if (userEntity == null) return result;
		
		List<AddressEntity> addresses = addressRepository.findAllByUserDto(userEntity);
		
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		for (AddressEntity address : addresses) {
			result.add(modelMapper.map(address, AddressDTO.class));
		}
		
		return result;
	}
	
	@Override
	public AddressDTO getAddress(String addressId) {
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		ModelMapper mapper = new ModelMapper();
		AddressDTO addressDTO = mapper.map(addressEntity, AddressDTO.class);
		
		return addressDTO;
	}

}
