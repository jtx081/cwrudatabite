package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query(value = "SELECT * FROM user u WHERE u.username = ?1", nativeQuery = true)
	User findByUsername(String username);
	
	@Query(value = "SELECT * FROM user u WHERE u.companyID = ?1 AND u.account_type='Company Admin'", nativeQuery = true)
	User findByCompanyID(int companyID);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM user u WHERE u.username = ?1", nativeQuery= true)
	void deleteFromId(String username);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE user u SET u.password=?2 WHERE u.username = ?1", nativeQuery= true)
	void updatePassword(String username, String password);
	
	@Query(value = "SELECT * FROM user u WHERE u.username = ?1 AND u.account_type='Company Admin'", nativeQuery = true)
	User findCompanyAccount(String username);
	
	@Query(value = "SELECT u.companyID FROM user u WHERE u.username = ?1", nativeQuery = true)
	int getCompanyID(String username);
	
	@Query(value = "SELECT * FROM user u WHERE u.companyID = ?1 AND u.account_type!='Company Admin'", nativeQuery = true)
	List<User> getUsersWithCompanyID(int companyID);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE user u SET u.companyID=0 WHERE u.username = ?1", nativeQuery= true)
	void removeCompanyID(String username);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE user u SET u.companyID=0 WHERE u.companyID = ?1", nativeQuery= true)
	void updateCompanyID(int companyID);
	
	
	
	
}
