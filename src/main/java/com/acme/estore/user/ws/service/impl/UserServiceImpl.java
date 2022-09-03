package com.acme.estore.user.ws.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.acme.estore.user.ws.io.entity.PasswordResetTokenEntity;
import com.acme.estore.user.ws.io.entity.UserEntity;
import com.acme.estore.user.ws.io.repository.PasswordResetTokenRepository;
import com.acme.estore.user.ws.io.repository.UserRepository;
import com.acme.estore.user.ws.model.ErrorMessage;
import com.acme.estore.user.ws.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	
	
	/**
	 * Creates the user from the JSON/XML request pay-load provided by the client
	 * Note: At least one address must be provided or a NullPointer Exception will occur.
	 * 
	 * @param UserDTO:  The user data transfer object that contains the requested user information pay-load
	 * @return UserDTO: The result that was entered into the database.
	 */
	
	@Override
	public UserDTO createUser(UserDTO userDto) {
			
		LOGGER.debug("Creating user");
		
		if (userRepository.findByEmail(userDto.getEmail()) != null) {
			LOGGER.error(ErrorMessage.RECORD_ALREADY_EXIST.getErrorMessage());
			throw new UserServiceException(ErrorMessage.RECORD_ALREADY_EXIST.getErrorMessage());
		}
		
		if (userDto.getAddress() != null) {
			for(int i=0; i < userDto.getAddress().size(); i++) {
				AddressDTO address = userDto.getAddress().get(i);
				address.setUserDto(userDto);
				address.setAddressId(utils.generateAddressId(30));
				userDto.getAddress().set(i, address);
			}
		}
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		
		String userId = utils.generateUserId(30);
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(userId));
		userEntity.setEmailVerificationStatus(Boolean.FALSE);
		
		UserEntity storedUserRecord = userRepository.save(userEntity);
		
		UserDTO result = modelMapper.map(storedUserRecord, UserDTO.class);
		
		LOGGER.debug("User created");
		
		return result;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		LOGGER.debug("Loading user by user name");
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		LOGGER.debug("Successfully Loaded user by user name");
				
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity.getEmailVerificationStatus(), 
				true, true, true, new ArrayList<>());
	}
	
	public UserDTO getUser(String email) {
		
		LOGGER.debug("Retrieving user by email address");
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) {
			LOGGER.error(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
			throw new UsernameNotFoundException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userEntity, userDTO);		
		
		LOGGER.debug("Successfully retrieved user by email address");
		
		return userDTO;
		
	}
	
	public UserDTO getUserById(String userId) {
		
		LOGGER.debug("Retrieving user by user ID");
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			LOGGER.error(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		ModelMapper mapper = new ModelMapper();
		UserDTO userDTO = mapper.map(userEntity, UserDTO.class);
		
		LOGGER.debug("Successfully retrieved user by user ID");
		
		return userDTO;
		
	}
	
	public UserDTO updateUser(String userId, UserDTO userDto) {
		
		LOGGER.debug("Updating user by user ID");
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			LOGGER.error(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity updatedUser = userRepository.save(userEntity);
		
		ModelMapper mapper = new ModelMapper();
		UserDTO userDTO = mapper.map(updatedUser, UserDTO.class);
		
		LOGGER.debug("Succesfully updated user by user ID");
		
		return userDTO;
	}
	
	public void deleteUser(String userId) {
		
		LOGGER.debug("Deleting user by user ID");
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			LOGGER.error(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		LOGGER.debug("Succesfully deleted user by user ID");
		
		userRepository.delete(userEntity);
		
	}
	 
	public List<UserDTO> getUsers(int page, int limit) {
			
		LOGGER.debug("Retrieving list of users");
		
		Pageable pageRequest = PageRequest.of(page, limit);
		
		Page<UserEntity> usersPage = userRepository.findAll(pageRequest);
		List<UserEntity> users = usersPage.getContent();
		
		List<UserDTO> result = users.stream()
			.map(u -> new UserDTO(u.getUserId(), u.getFirstName(), u.getLastName(), u.getEmail()))
			.collect(Collectors.toList());
		
		LOGGER.debug("Successfully retrieved list of users");
		
		return result;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		boolean result = false;
		
		LOGGER.debug("Requesting password reset");
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) {
			LOGGER.error(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
			throw new UserServiceException(ErrorMessage.NO_RECORD_FOUND.getErrorMessage());
		}
		
		String token = new Utils().generatePasswordResetToken(userEntity.getUserId());
		
		PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
		passwordResetTokenEntity.setToken(token);
		passwordResetTokenEntity.setUserDetails(userEntity);
		
		passwordResetTokenRepository.save(passwordResetTokenEntity);
		
		result = new AmazonSESImpl().sendPasswordResetRequest(userEntity.getFirstName(), userEntity.getEmail(), token);
		
		
		LOGGER.debug("Successfully reseted password");
		
		return result;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		boolean result = false;
		
		UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);
		
		if (userEntity != null) {
			boolean hasTokenExpired = Utils.hasTokenExpired(token);
			if (!hasTokenExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepository.save(userEntity);
				result = true;
			}
		}
		
		return result;
	}

}
