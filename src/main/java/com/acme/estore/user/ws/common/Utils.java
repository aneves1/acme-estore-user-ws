package com.acme.estore.user.ws.common;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.acme.estore.user.ws.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
	
	public String generateEmailVerificationToken(String userId) {
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
				.compact();
		
		return token;
	}
	
	public String generatePasswordResetToken(String userId) {
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
				.compact();
		
		return token;
	}
	
	private String generateRandomString(int length) {
		StringBuilder string = new StringBuilder(length);
		
		for (int i = 0; i < length; i++) {
			string.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return string.toString();
	}
	
	public static boolean hasTokenExpired(String token) {
		Claims claims = Jwts.parser()
			.setSigningKey(SecurityConstants.getTokenSecret())
			.parseClaimsJws(token)
			.getBody();
			
			Date tokenExpirationDate = claims.getExpiration();
			Date todayDate = new Date();
			
			return tokenExpirationDate.before(todayDate);
	}
	
}
