package com.tech.mkblogs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.tech.mkblogs.security.db.DBAuthProvider;
import com.tech.mkblogs.security.inmemory.InMemoryAuthProvider;

@Component
public class AccountAuthenticateManager implements AuthenticationManager{

	
	private AuthenticationProvider provider;
	
	@Autowired
	ApplicationContext context;
	
	public AccountAuthenticateManager() {		
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		provider = context.getBean(InMemoryAuthProvider.class);
		Authentication auth = provider.authenticate(authentication);
		if(!auth.isAuthenticated()) {
			provider = context.getBean(DBAuthProvider.class);
			auth = provider.authenticate(authentication);
		}
		return auth;
	}
}
