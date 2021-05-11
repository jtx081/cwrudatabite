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
@Rollback(false)
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
	public void testFindUserByEmail() {
		String username = "test1@domain.com";
		
		User user = repo.findByUsername(username);
		
		assertThat(user).isNotNull();
	}
}
