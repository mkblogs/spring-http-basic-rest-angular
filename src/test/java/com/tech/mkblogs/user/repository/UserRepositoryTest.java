package com.tech.mkblogs.user.repository;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.tech.mkblogs.model.Authorities;
import com.tech.mkblogs.model.User;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	@Before
    public void beforeEachTest() {
		log.info("==================================================================================");
		log.info("This is executed before each Test");
    }
	@After
    public void afterEachTest() {
		log.info("This is exceuted after each Test");
		log.info("==================================================================================");
    }
	@Test
	public void testRepositoryObject() {
		assertNotNull(userRepository);
	}
	
	@Test
	public void testSaveObject() {
		User user = new User();
		user.setLoginName("First");
		user.setPassword("first");
		user.setEncrypted(false);
		user.setFirstName("FirstName");
		user.setLastName("lastname");
		
		Authorities authorities = new Authorities();
		authorities.setAuthority("ROLE_USER");
		authorities.setUser(user);
		
		user.addAuthorities(authorities);		
		userRepository.save(user);
		assertNotNull(user.getId());
	}
	
	@Test
	public void testUpdateObject() {
		User user = new User();
		user.setLoginName("Second");
		user.setPassword("second");
		user.setEncrypted(false);
		user.setFirstName("second");
		user.setLastName("lastname");
		
		Authorities authorities = new Authorities();
		authorities.setAuthority("ROLE_USER");
		authorities.setUser(user);
		
		user.addAuthorities(authorities);		
		userRepository.save(user);
		assertNotNull(user.getId());
		user.setFirstName("second_update");
		userRepository.save(user);
	}
	
	@Test
	public void testDeleteObject() {
		User user = new User();
		user.setLoginName("Second");
		user.setPassword("second");
		user.setEncrypted(false);
		user.setFirstName("second");
		user.setLastName("lastname");
		
		Authorities authorities = new Authorities();
		authorities.setAuthority("ROLE_USER");
		authorities.setUser(user);
		
		user.addAuthorities(authorities);		
		userRepository.save(user);
		assertNotNull(user.getId());		
		userRepository.deleteById(user.getId());
	}
	
	@Test
	@Transactional
	public void testGetAllData() {
		List<User> userList = userRepository.findAll();
		userList.stream().forEach((user) -> log.info(user));
	}
	
	@Test
	public void getLogin() {
		String loginName = "mkblogs";		
		User user = userRepository.findByLoginName(loginName);
		log.info(user);
		
	}
}
