package com.tech.mkblogs.security;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.mkblogs.model.User;
import com.tech.mkblogs.user.repository.UserRepository;

@Service
public class AccountUserService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	UserRepository repository;
	
	
	public User getUser(String username) {
		User loginUser = repository.findByLoginName(username);
		return loginUser;
	}
}
