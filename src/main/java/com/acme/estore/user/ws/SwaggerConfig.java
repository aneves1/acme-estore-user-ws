package com.acme.estore.user.ws;


import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.core.util.Constants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;


@Configuration
public class SwaggerConfig {
	
	private static final String API_KEY = "bearerAuth";
  
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(API_KEY,apiKeySecuritySchema())) // define the apiKey SecuritySchema
                .info(new Info().title("Title API").description(
                        "RESTful services documentation with OpenAPI 3."))
                .security(Collections.singletonList(new SecurityRequirement().addList(API_KEY))); // then apply it. If you don't apply it will not be added to the header in cURL
    }

    public SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme()
                .name("authorization") // authorisation-token
                .description("Description about the TOKEN")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY);
    }
	  
}
