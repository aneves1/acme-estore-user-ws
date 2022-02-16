package com.acme.estore.user.ws.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.acme.estore.user.ws.common.Utils;
import com.acme.estore.user.ws.dto.AddressDTO;
import com.acme.estore.user.ws.dto.UserDTO;
import com.acme.estore.user.ws.exception.UserServiceException;
import com.acme.estore.user.ws.io.entity.UserEntity;
import com.acme.estore.user.ws.io.repository.UserRepository;
import com.acme.estore.user.ws.model.Address;
import com.acme.estore.user.ws.model.ErrorMessage;
import com.acme.estore.user.ws.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDTO createUser(UserDTO userDto) {
			
		if (userRepository.findByEmail(userDto.getEmail()) != null) {
			throw new UserServiceException(ErrorMessage.RECORD_ALREADY_EXIST.getErrorMessage());
		}
		
		for(int i=0; i < userDto.getAddress().size(); i++) {
			AddressDTO address = userDto.getAddress().get(i);
			address.setUserDto(userDto);
			address.setAddressId(utils.generatedAddressId(30));
			userDto.getAddress().set(i, address);
		}
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		
		String userId = utils.generateUserId(30);
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		
		UserEntity storedUserRecord = userRepository.save(userEntity);
		
		UserDTO result = modelMapper.map(storedUserRecord, UserDTO.class);
		
		return result;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}
	
	public UserDTO getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userEntity, userDTO);
		
		return userDTO;
		
	}
	
	public UserDTO getUserById(String userId) {
		UserDTO userDTO = new UserDTO();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		BeanUtils.copyProperties(userEntity, userDTO);
		
		return userDTO;
		
	}
	
	public UserDTO updateUser(String userId, UserDTO userDto) {
		UserDTO userDTO = new UserDTO();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity updatedUser = userRepository.save(userEntity);
		
		
		BeanUtils.copyProperties(updatedUser, userDTO);
		
		return userDTO;
	}
	
	public void deleteUser(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userRepository.delete(userEntity);
		
	}
	 
	public List<UserDTO> getUsers(int page, int limit) {
			
		Pageable pageRequest = PageRequest.of(page, limit);
		
		Page<UserEntity> usersPage = userRepository.findAll(pageRequest);
		List<UserEntity> users = usersPage.getContent();
		
		List<UserDTO> result = users.stream()
			.map(u -> new UserDTO(u.getUserId(), u.getFirstName(), u.getLastName(), u.getEmail()))
			.collect(Collectors.toList());
		
		return result;
	}

}
