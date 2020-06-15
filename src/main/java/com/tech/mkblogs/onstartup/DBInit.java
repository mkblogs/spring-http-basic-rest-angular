package com.tech.mkblogs.onstartup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tech.mkblogs.model.Authorities;
import com.tech.mkblogs.model.User;
import com.tech.mkblogs.user.repository.UserRepository;

@Component
public class DBInit {

	@Autowired
	UserRepository userRepository;	
	
	public void loadUsers() {
		if(userRepository.count() == 0) {
			User plainUser = getMYSQLPlainUser();
			userRepository.save(plainUser);
			User plainAdmin = getMYSQLPlainAdmin();
			userRepository.save(plainAdmin);
		}
	}
	
	private User getMYSQLPlainUser() {
		User user = new User();
		user.setLoginName("user");
		user.setPassword("password");
		user.setCreatedName("test");
		user.setFirstName("First Mysql");
		user.setLastName("Last Mysql");
		user.setEnabled(Boolean.FALSE);
		user.setAccountExpired(false);	
		user.setEncrypted(Boolean.FALSE);
		Authorities authorities = new Authorities();
		authorities.setAuthority("ROLE_USER");
		user.addAuthorities(authorities);
		return user;
	}
	
	private User getMYSQLPlainAdmin() {
		User user = new User();
		user.setLoginName("admin");
		user.setPassword("password");
		user.setCreatedName("test");
		user.setFirstName("Admin Mysql");
		user.setLastName("Admin Mysql");
		user.setEnabled(Boolean.FALSE);
		user.setEncrypted(Boolean.FALSE);
		Authorities authorities = new Authorities();
		authorities.setAuthority("ROLE_ADMIN");
		user.addAuthorities(authorities);
		return user;
	}
}
