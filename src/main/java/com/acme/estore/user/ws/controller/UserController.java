package com.acme.estore.user.ws.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@GetMapping(path="/{userId}", produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserResponse getUser(@PathVariable String userId) {
				
		UserDTO userDTO = userService.getUserById(userId);
		
		ModelMapper mapper = new ModelMapper();
		UserResponse response = mapper.map(userDTO, UserResponse.class);
		
		return response;
	}
	
	@PostMapping(
		consumes= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
		produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
		)
	public UserResponse createUser(@RequestBody User user) throws UserServiceException {	
		
		if (user.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessage.MISSING_REQUIRED_FIELD.getErrorMessage());
		}
		
		ModelMapper mapper = new ModelMapper();
		UserDTO userDto = mapper.map(user, UserDTO.class);
		
		UserDTO createdUser  = userService.createUser(userDto);
		
		UserResponse response = mapper.map(createdUser, UserResponse.class);		
		
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
		
		ModelMapper mapper = new ModelMapper();
		UserDTO userDTO = mapper.map(user, UserDTO.class);
		
		UserDTO updatedUser  = userService.updateUser(userId, userDTO);
		mapper.map(updatedUser, UserResponse.class);
		
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
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
	public CollectionModel<AddressResponse> getUserAddresses(@PathVariable String userId) {
		List<AddressResponse> addresses = new ArrayList<>();
		
		List<AddressDTO> addressDTO = addressService.getAddresses(userId);
		
		if (Optional.ofNullable(addressDTO).isPresent()) {
			java.lang.reflect.Type listType = new TypeToken<List<AddressResponse>>() {}.getType();		
			addresses = new ModelMapper().map(addressDTO, listType);
			
			for (AddressResponse address : addresses) {
				Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserAddress(userId, address.getAddressId())).withSelfRel();
				address.add(selfLink);
			}
			
		}
	
		Link userLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
		Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserAddresses(userId)).withSelfRel();
		
		return CollectionModel.of(addresses, userLink, selfLink);
		
	}

	@GetMapping(path="/{userId}/address/{addressId}",
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public EntityModel<AddressResponse> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		
		AddressDTO addressesDTO = addressService.getAddress(addressId);
		
		ModelMapper mapper = new ModelMapper();
		AddressResponse address = mapper.map(addressesDTO, AddressResponse.class);
		
		
        EntityModel<AddressResponse> entityModel = EntityModel.of(address);
        Link addressLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
        Link addressesLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
        
        
        entityModel.add(addressLink);
        entityModel.add(userLink);
        entityModel.add(addressesLink);
		
		return entityModel;
		
	}
}
