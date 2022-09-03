package com.acme.estore.user.ws.io.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.acme.estore.user.ws.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	UserEntity findUserByEmailVerificationToken(String token);
	
	@Query(value="select * from User u where u.USER_EMAIL_VERIFICATION_STATUS = 'true'",
		   countQuery="select count(*) from User u where u.USER_EMAIL_VERIFICATION_STATUS = 'true'",  // used by spring data jpa to know how to split number of records returned into pages
		   nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable request);
	
	//Note. when using positional parameters (i.e. ?1) you must follow the order
	@Query(value="select * from User u where u.user_first_name = ?1", nativeQuery=true)
	List<UserEntity> findUserByFirstName(String firstName);
	
	//Note. when using positional parameters (i.e. ?1) you must follow the order
	@Query(value="select * from User u where u.user_last_name = :lastName", nativeQuery=true)
	List<UserEntity> findUserByLastName(@Param("lastName") String lastName);
	
	//Note. when using positional parameters (i.e. ?1) you must follow the order
	@Query(value="select * from User u where u.user_first_name Like %:keyword% or u.user_last_name LIKE %:keyword%", nativeQuery=true)
	List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);
	
	@Modifying
	@Transactional
	@Query(value="update user u set u.USER_EMAIL_VERIFICATION_STATUS=:emailVerificationStatus where u.USER_ID=:userId", nativeQuery=true)
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);
	
	// query using JPQL
	@Query("select u from UserEntity u where u.userId=:userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);
}
