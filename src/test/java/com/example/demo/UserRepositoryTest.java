package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)
public class UserRepositoryTest {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		User user = new User();
		user.setUsername("email1@domain.com"); 
		user.setPassword("password");
		
		User savedUser = repo.save(user);
		
		User existUser = entityManager.find(User.class, savedUser.getUsername());
		
		assertThat(existUser.getUsername()).isEqualTo(user.getUsername());
	}
	
	@Test
	public void testFindByUsername() {
		String username = "test1@domain.com";
		
		User user = repo.findByUsername(username);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testGetAccountType() {
		User user = new User();

		user.setAccountType("Individual");
		
		assertThat(user.getAccountType()).isEqualTo("Individual");
	}
	
	@Test
	public void testGetCompanyId() {
		User user = new User();

		user.setCompanyID(0);
		
		assertThat(user.getCompanyID()).isEqualTo(0);
	}
	
	@Test
	public void testGetPassword() {
		User user = new User();

		user.setPassword("testpass");
		
		assertThat(user.getPassword()).isEqualTo("testpass");
	}
	
	@Test
	public void testGetCompanyName() {
		User user = new User();

		user.setCompanyName("comp");
		
		assertThat(user.getCompanyName()).isEqualTo("comp");
	}
	
	@Test
	public void testFindByCompanyId() {
		User user = repo.findByCompanyID(850170);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testDeleteFromId() {
		repo.deleteFromId("test1@domain.com");
		
		assertThat(repo.findByUsername("test1@domain.com")).isNull();
	}
	
	@Test
	public void testFindCompanyAccount() {
		assertThat(repo.findCompanyAccount("testcomp1@domain.com")).isNotNull();
	}
	
	@Test
	public void testGetCompanyID() {
		assertThat(repo.getCompanyID("testcomp1@domain.com")).isEqualTo(850170);
	}
	
	@Test
	public void testGetUsersWithCompanyID() {
		assertThat(repo.getUsersWithCompanyID(850170).size()).isEqualTo(1);
	}
	
	@Test
	public void testRemoveCompanyId() {
		repo.removeCompanyID("test1@domain.com");
		assertThat(repo.getCompanyID("test1@domain.com")).isEqualTo(0);
	}
	
	@Test
	public void testUpdateCompanyId() {
		repo.updateCompanyID(850170);
		assertThat(repo.getCompanyID("test1@domain.com")).isEqualTo(0);
	}
}
