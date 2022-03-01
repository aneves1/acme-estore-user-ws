package com.acme.estore.user.ws.service;

import com.acme.estore.user.ws.dto.UserDTO;

public interface AmazonSES {
	void verifyEmail(UserDTO userDTO);
	boolean sendPasswordResetRequest(String firstName, String email, String token);
}
