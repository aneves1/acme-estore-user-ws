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
	
	/**
	 * Creates the user from the JSON/XML request pay-load provided by the client
	 * Note: At least one address must be provided or a NullPointer Exception will occur.
	 * 
	 * @param UserDTO:  The user data transfer object that contains the requested user information pay-load
	 * @return UserDTO: The result that was entered into the database.
	 */
	
	@Override
	public UserDTO createUser(UserDTO userDto) {
			
		if (userRepository.findByEmail(userDto.getEmail()) != null) {
			throw new UserServiceException(ErrorMessage.RECORD_ALREADY_EXIST.getErrorMessage());
		}
		
		// getAddress() must not return null
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

// Issues with mapper here: Converter org.modelmapper.internal.converter.MergingCollectionConverter@77e4d335 failed to convert org.hibernate.collection.internal.PersistentBag to java.util.List
//		ModelMapper mapper = new ModelMapper();
//		UserDTO userDTO = mapper.map(userEntity, UserDTO.class);
		
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userEntity, userDTO);		
		
		return userDTO;
		
	}
	
	public UserDTO getUserById(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		ModelMapper mapper = new ModelMapper();
		UserDTO userDTO = mapper.map(userEntity, UserDTO.class);
		
		
		return userDTO;
		
	}
	
	public UserDTO updateUser(String userId, UserDTO userDto) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity updatedUser = userRepository.save(userEntity);
		
		ModelMapper mapper = new ModelMapper();
		UserDTO userDTO = mapper.map(updatedUser, UserDTO.class);
		
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
