package com.acme.estore.user.ws.common;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

	@Autowired
	Utils utils;
	
	final String USER_ID = "bfhrw3982jgsh2030pgh";
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGenerateUserId() {
		String userId1 = utils.generateUserId(30);
		String userId2 = utils.generateUserId(30);
		
		assertNotNull(userId1);
		assertNotNull(userId2);
		
		// assert that userId1 and userId2 are unique values
		assertTrue(userId1.length() == 30);
		assertTrue(!userId1.equals(userId2));
	}

	@Test
	void testHasTokenNotExpired() {
		String token = utils.generateEmailVerificationToken(USER_ID);
		assertNotNull(token);
		
		boolean hasTokenExpired = Utils.hasTokenExpired(token);
		assertFalse(hasTokenExpired);
		
	}
	
	@Test
	@Disabled
	void testHasTokenExpired() {
		String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiXREZ1S1FPaEptUzhIVjRGSXU3VkUycTJhRXM5ODciLCJleHAiOjE2NDcwOTU2ODd9.DnR8P4d7_K1u4JI3UQNDnj6suIexInj8qEm3KxZlb7jbujI0-LP8imJS-_n2_hujVy5MmFWAB8tlI06N5Ew";
		assertNotNull(expiredToken);
		
		boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
		assertTrue(hasTokenExpired);
		
	}

}
