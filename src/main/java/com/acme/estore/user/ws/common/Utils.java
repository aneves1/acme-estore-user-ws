package com.acme.estore.user.ws.common;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public String generateUserId(int length) {
		return generateRandomString(length);
	}
	
	public String generatedAddressId(int length) {
		return generateRandomString(length);
	}
	
	private String generateRandomString(int length) {
		StringBuilder string = new StringBuilder(length);
		
		for (int i = 0; i < length; i++) {
			string.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return string.toString();
	}
	
}
