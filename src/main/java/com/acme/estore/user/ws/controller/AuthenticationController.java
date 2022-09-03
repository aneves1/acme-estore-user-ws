package com.acme.estore.user.ws.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acme.estore.user.ws.request.LoginRequestModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
public class AuthenticationController {

	@Operation(summary = "User Login",
            responses = { 
            		@ApiResponse(responseCode = "200", description = "OK", headers = {
            					@Header(name = "Authorization", description="Bearer <JWT Token>", schema=@Schema(implementation=String.class)),
            					@Header(name = "userId", description="Public User Id", schema=@Schema(implementation=String.class))
            				}),
            		@ApiResponse(responseCode = "201", description = "Created"),
            		@ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "403 Forbidden"),
            		@ApiResponse(responseCode = "404", description = "404 Not Found") 
            })
	@PostMapping("/user/login")
	public void fakeLogin(@RequestBody LoginRequestModel model) {
		throw new IllegalStateException("This mehtod should not be called.  This method is implmented by Spring Security");
	}
}
