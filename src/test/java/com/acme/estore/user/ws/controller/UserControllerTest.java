package com.acme.estore.user.ws.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.acme.estore.user.ws.dto.AddressDTO;
import com.acme.estore.user.ws.dto.UserDTO;
import com.acme.estore.user.ws.io.entity.AddressEntity;
import com.acme.estore.user.ws.response.UserResponse;
import com.acme.estore.user.ws.service.UserService;

class UserControllerTest {
	
	@InjectMocks
	UserController userController;
	
	@Mock
	UserService userService;

	UserDTO userDto;
	
	final String USER_ID = "bfhrw3982jgsh2030pgh";
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userDto = new UserDTO();
		userDto.setAddress(getAddressesDto());
		userDto.setFirstName("Anthony");
		userDto.setLastName("Neves");
		userDto.setPassword("12345678");
		userDto.setEmail("test@test.com");
		userDto.setUserId(USER_ID);
		userDto.setAddress(getAddressesDto());
		userDto.setPassword("0oknmcd02q");
	}

	@Test
	void testGetUser() {
		
		when(userService.getUserById(anyString())).thenReturn(userDto);
		
		UserResponse response = userController.getUser(USER_ID);
		
		assertNotNull(response);
		assertEquals(USER_ID, response.getUserId());
		assertEquals(userDto.getFirstName(), response.getFirstName());
		assertEquals(userDto.getLastName(), response.getLastName());
		assertTrue(userDto.getAddress().size() == response.getAddress().size());
		
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
