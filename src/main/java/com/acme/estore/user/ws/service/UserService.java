package com.acme.estore.user.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.acme.estore.user.ws.dto.UserDTO;
import com.acme.estore.user.ws.model.Address;

public interface UserService extends UserDetailsService {
	UserDTO createUser(UserDTO userDto);
	UserDTO getUser(String email);
	UserDTO getUserById(String id);
	UserDTO updateUser(String userId, UserDTO userDto);
	void deleteUser(String userId);
	List<UserDTO> getUsers(int page, int limit);
	boolean requestPasswordReset(String email);
	boolean verifyEmailToken(String token);
}
