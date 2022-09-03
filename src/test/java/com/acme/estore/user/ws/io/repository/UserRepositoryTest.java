package com.acme.estore.user.ws.io.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.acme.estore.user.ws.io.entity.AddressEntity;
import com.acme.estore.user.ws.io.entity.UserEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	

	@BeforeEach
	void setUp() throws Exception {
		
		// Initialize UserEntity
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("george");
		userEntity.setLastName("jones");
		userEntity.setUserId("co30lsc2w0");
		userEntity.setEncryptedPassword("xxx");
		userEntity.setEmail("george.jones@gmail.com");
		userEntity.setEmailVerificationStatus(true);
		userEntity.setEmailVerificationToken("20lsdsljf203r4sdfs");
		
		//Initialize User Address
		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setCountry("US");
		addressEntity.setStreet("1 main st.");
		addressEntity.setCity("Jupiter");
		addressEntity.setZipCode("33121");
		addressEntity.setType("shipping");
		addressEntity.setAddressId("ashoy023432");
		
		List<AddressEntity> userAddresses = new ArrayList<>();
		userAddresses.add(addressEntity);
		
		userEntity.setAddress(userAddresses);
		
		userRepository.save(userEntity);
		
	}

	@Test
	void testGetVerifiedUsers() {
		Pageable request = PageRequest.of(0, 2);
		
		Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(request);
		assertNotNull(pages);
		
		List<UserEntity> userEntities = pages.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);
		
		
	}
	
	@Test
	final void testFindUserByFirstName() {
		String firstName = "george";
		
		List<UserEntity> userEntities = userRepository.findUserByFirstName(firstName);
		
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);
		
		UserEntity user = userEntities.get(0);
		assertTrue(user.getFirstName().equals(firstName));
		
	}
	
	
	 @Test 
	 final void testFindUserByLastName() { 
		 String lastName = "jones";
		 List<UserEntity> userEntities = userRepository.findUserByLastName(lastName);
		 assertNotNull(userEntities); 
		 assertTrue(userEntities.size() == 1);
		
		 UserEntity user = userEntities.get(0);
		 assertTrue(user.getLastName().equals(lastName));	 
	 }
	 
	 @Test
	 final void testUpdateUserEmailVerificationStatus() {
		 boolean userEmailVerificationStatus = false;
		 
		 userRepository.updateUserEmailVerificationStatus(userEmailVerificationStatus, "co30lsc2w0");
		 
		 UserEntity userEntity = userRepository.findByUserId("co30lsc2w0");
		 
		 boolean storedEmailVerficationStatus = userEntity.getEmailVerificationStatus();
		 
		 assertTrue(storedEmailVerficationStatus == userEmailVerificationStatus);
		 
		 
	 }
	 
	 @Test
	 final void testfindUserEntityByUserId() {
		 String userId = "co30lsc2w0";
		 
		 UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
		 
		 assertNotNull(userEntity);
		 assertTrue(userEntity.getUserId() == userId);
	 }

}
