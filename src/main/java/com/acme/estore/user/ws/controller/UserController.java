package com.acme.estore.user.ws.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acme.estore.user.ws.dto.AddressDTO;
import com.acme.estore.user.ws.dto.UserDTO;
import com.acme.estore.user.ws.exception.UserServiceException;
import com.acme.estore.user.ws.model.AddressResponse;
import com.acme.estore.user.ws.model.ErrorMessage;
import com.acme.estore.user.ws.model.OperationStatus;
import com.acme.estore.user.ws.model.OperationStatusResponse;
import com.acme.estore.user.ws.model.User;
import com.acme.estore.user.ws.model.UserResponse;
import com.acme.estore.user.ws.service.AddressService;
import com.acme.estore.user.ws.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@GetMapping(path="/{userId}", produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserResponse getUser(@PathVariable String userId) {
		UserResponse response = new UserResponse();
		
		UserDTO userDTO = userService.getUserById(userId);
		BeanUtils.copyProperties(userDTO, response);
		
		return response;
	}
	
	@PostMapping(
		consumes= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
		produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
		)
	public UserResponse createUser(@RequestBody User user) throws UserServiceException {	
		UserResponse response = new UserResponse();
		
		if (user.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessage.MISSING_REQUIRED_FIELD.getErrorMessage());
		}
		
		ModelMapper modelMapper = new ModelMapper();
		UserDTO userDto = modelMapper.map(user, UserDTO.class);
		
		UserDTO createdUser  = userService.createUser(userDto);
		modelMapper.map(createdUser, response);		
		
		return response;
	}
	
	@PutMapping(path="/{userId}",	
		consumes= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
		produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserResponse updateUser(@PathVariable String userId, @RequestBody User user) {
		UserResponse response = new UserResponse();
		
		if (user.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessage.MISSING_REQUIRED_FIELD.getErrorMessage());
		}
		
		UserDTO userDto = new UserDTO();
		BeanUtils.copyProperties(user, userDto);
		
		UserDTO updateUser  = userService.updateUser(userId, userDto);
		BeanUtils.copyProperties(updateUser, response);		
		
		return response;
	}
	
	@DeleteMapping(path="/{userId}", produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusResponse deleteUser(@PathVariable String userId) {
		OperationStatusResponse response = new OperationStatusResponse();
		
		response.setName(RequestOperation.DELETE.name());
		
		userService.deleteUser(userId);
		
		response.setStatus(OperationStatus.SUCCESS.name());
		
		return response;
	}
	
	@GetMapping(produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserResponse> getUsers(@RequestParam(value="page", defaultValue="0") int page, 
			@RequestParam(value="limit", defaultValue="25") int limit) {
		
		List<UserDTO> users = userService.getUsers(page, limit);
				
		List<UserResponse> response = users.stream()
				.map(u -> new UserResponse(u.getUserId(), u.getFirstName(), u.getLastName(), u.getEmail()))
				.collect(Collectors.toList());
		
		return response;
	}
	
	@GetMapping(path="/{userId}/addresses",
			consumes= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<AddressResponse> getUserAddresses(@RequestParam(value="userId") String userId) {
		List<AddressResponse> response = new ArrayList<>();
		
		java.lang.reflect.Type listType = new TypeToken<List<AddressResponse>>() {}.getType();
		
		List<AddressDTO> addressDTO = addressService.getAddress(userId);
		
		ModelMapper mapper = new ModelMapper();
		response = mapper.map(addressDTO, listType);
		
		
		return response;
		
	}

}
