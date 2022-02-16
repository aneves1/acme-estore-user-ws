package com.acme.estore.user.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.acme.estore.user.ws.security.ApplicationProperties;

@SpringBootApplication
public class AcmeEstoreUserWsApplication extends SpringBootServletInitializer {
	
	

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AcmeEstoreUserWsApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AcmeEstoreUserWsApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}
	
	@Bean("ApplicationProperties")
	public ApplicationProperties getApplicationProperties() {
		return new ApplicationProperties();
	}

}
