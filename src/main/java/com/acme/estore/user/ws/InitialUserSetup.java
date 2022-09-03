package com.acme.estore.user.ws;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.acme.estore.user.ws.common.Utils;
import com.acme.estore.user.ws.io.entity.AuthorityEntity;
import com.acme.estore.user.ws.io.entity.RoleEntity;
import com.acme.estore.user.ws.io.entity.UserEntity;
import com.acme.estore.user.ws.io.repository.AuthorityRepository;
import com.acme.estore.user.ws.io.repository.RoleRepository;
import com.acme.estore.user.ws.io.repository.UserRepository;

@Component
public class InitialUserSetup {
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	@EventListener
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("event called");
		
		AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority = createAuthority("WRITE_AUHTORITY");
		AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");
		
		RoleEntity roleUser = createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
		RoleEntity roleAdmin = createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
		
		if (roleAdmin == null) return;
		
		UserEntity adminUser = new UserEntity();
		adminUser.setFirstName("Krista");
		adminUser.setLastName("Meyer");
		adminUser.setEmail("krista.meyer.daugherty.com");
		adminUser.setEmailVerificationStatus(true);
		adminUser.setEmailVerificationToken("krista.meyer.daugherty.com");
		adminUser.setUserId(utils.generateUserId(30));
		adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("w3rwesdfs"));
		adminUser.setRoles(Arrays.asList(roleAdmin));
		
		userRepository.save(adminUser);
		
	}
	
	@Transactional
	private AuthorityEntity createAuthority(String name) {
		
		AuthorityEntity authority = authorityRepository.findByAuthorityName(name);
		if (authority == null) {
			authority = new AuthorityEntity(name);
			authorityRepository.save(authority);
		}
		
		return authority;
	}
	
	@Transactional
	private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
		
		RoleEntity role = roleRepository.findByRoleName(name);
		if (role == null) {
			role = new RoleEntity(name);
			role.setAuthorities(authorities);
			roleRepository.save(role);
		}
		
		return role;
	}
}
