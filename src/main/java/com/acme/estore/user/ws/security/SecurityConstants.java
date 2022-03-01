package com.acme.estore.user.ws.security;

import com.acme.estore.user.ws.SpringApplicationContext;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 864000000;
	public static final long PASSWORD_RESET_EXPIRATION_TIME = 360000;
	public static final String TOKEN_PREFIX = "Bearer";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/user";
	public static final String VERIFICATION_EMAIL_URL = "/user/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "/user/password-reset";
    public static final String PASSWORD_RESET_URL = "/user/reset-password";
    public static final String H2_CONSOLE = "/h2-console/**";
	
	public static String getTokenSecret() {
		ApplicationProperties properties = (ApplicationProperties) SpringApplicationContext.getBean("ApplicationProperties");
		return properties.getTokenSecret();
	}
}
