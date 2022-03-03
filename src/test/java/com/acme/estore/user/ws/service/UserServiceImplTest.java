package com.acme.estore.user.ws.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.acme.estore.user.ws.common.Utils;
import com.acme.estore.user.ws.dto.AddressDTO;
import com.acme.estore.user.ws.dto.UserDTO;
import com.acme.estore.user.ws.exception.UserServiceException;
import com.acme.estore.user.ws.io.entity.AddressEntity;
import com.acme.estore.user.ws.io.entity.UserEntity;
import com.acme.estore.user.ws.io.repository.UserRepository;
import com.acme.estore.user.ws.service.impl.UserServiceImpl;

class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	String userId = "0d0shhdlfs";
	String addressId = "0wlnokdlnv";
	String encryptedPassword = "sho0w200dcksrf";
	
	UserEntity userEntity;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Anthony");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("2w0kds0dfl");
		userEntity.setAddress(getAddressesEntity());
	}

	@Test
	void testGetUser() {
				
		when( userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDTO userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals("Anthony", userDto.getFirstName());
	}
	
	@Test
	final void testGetUser_UsernameNotFoundException() {
		
		when( userRepository.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class, () -> userService.getUser("test@test.com"));
		
	}
	
	@Test
	final void testCreateUser() {
			
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn(addressId);
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		UserDTO userDto = new UserDTO();
		userDto.setAddress(getAddressesDto());
		userDto.setFirstName("Anthony");
		userDto.setLastName("Neves");
		userDto.setPassword("12345678");
		userDto.setEmail("test@test.com");
		
		UserDTO storedUser = userService.createUser(userDto);
		
		assertNotNull(storedUser);
		assertEquals(userEntity.getFirstName(), storedUser.getFirstName());
		assertEquals(userEntity.getLastName(), storedUser.getLastName());
		assertNotNull(storedUser.getUserId());
		assertEquals(storedUser.getAddress().size(), userEntity.getAddress().size());
		verify(utils, times(2)).generateAddressId(30);
		verify(bCryptPasswordEncoder, times(1)).encode("12345678");
	}
	
	@Test
	final void testCreateUser_UserServiceException() {
		
		when( userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDTO userDto = new UserDTO();
		userDto.setAddress(getAddressesDto());
		userDto.setFirstName("Anthony");
		userDto.setLastName("Neves");
		userDto.setPassword("12345678");
		userDto.setEmail("test@test.com");
		
		assertThrows(UserServiceException.class, () -> userService.createUser(userDto));
		
	}
	
	private List<AddressDTO> getAddressesDto() {
		AddressDTO shippingAddressDto = new AddressDTO();
		shippingAddressDto.setType("shipping");
		shippingAddressDto.setStreet("111 Main St.");
		shippingAddressDto.setCity("Vancouver");
		shippingAddressDto.setCountry("Canada");
		shippingAddressDto.setZipCode("ABC123");
		
		AddressDTO billingAddressDto = new AddressDTO();
		shippingAddressDto.setType("billing");
		shippingAddressDto.setStreet("PO Box 1111.");
		shippingAddressDto.setCity("Vancouver");
		shippingAddressDto.setCountry("Canada");
		shippingAddressDto.setZipCode("ABC123");
		
		List<AddressDTO> addresses = new ArrayList<>();
		addresses.add(shippingAddressDto);
		addresses.add(billingAddressDto);
		
		return addresses;
	}
	
	private List<AddressEntity> getAddressesEntity() {
		List<AddressDTO> addresses = getAddressesDto();
		java.lang.reflect.Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		
		return new ModelMapper().map(addresses, listType);
	}
	

}
