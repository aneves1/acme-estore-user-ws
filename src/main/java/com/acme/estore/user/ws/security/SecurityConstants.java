package com.acme.estore.user.ws.security;

import com.acme.estore.user.ws.SpringApplicationContext;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 864000000;
	public static final String TOKEN_PREFIX = "Bearer";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/user";
	
	public static String getTokenSecret() {
		ApplicationProperties properties = (ApplicationProperties) SpringApplicationContext.getBean("ApplicationProperties");
		return properties.getTokenSecret();
	}
}
