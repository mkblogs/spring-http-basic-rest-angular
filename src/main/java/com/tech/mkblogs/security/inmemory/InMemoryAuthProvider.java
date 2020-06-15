package com.tech.mkblogs.security.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InMemoryAuthProvider implements AuthenticationProvider{

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal() + "";
	    String password = authentication.getCredentials() + "";
		if ("user".equalsIgnoreCase(username)) {
			return isValidInMemoryUser(username, password);
		}else {
			return isValidInMemoryAdmin(username, password);
		}
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param encyprted
	 * @return
	 */
	protected UsernamePasswordAuthenticationToken isValidInMemoryUser(String username,String password) {
		List<String> userRole = new ArrayList<String>();
		userRole.add("ROLE_USER_READ_ONLY");
		Collection<? extends GrantedAuthority> authorities 
				= userRole.stream().map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());
		
		if ("user".equalsIgnoreCase(username) && "user@123".equalsIgnoreCase(password)) {
		       return new UsernamePasswordAuthenticationToken
		              (username, null, authorities);
	    }else {
	    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(password);
			if ("user".equalsIgnoreCase(username) && encoder.matches("userenc@123",encodedPassword)){
		       return new UsernamePasswordAuthenticationToken
		              (username, null, authorities);
		    }else {
		    	return new UsernamePasswordAuthenticationToken(username,password);
		    }
	    }
	}
	
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param encyprted
	 * @return
	 */
	protected UsernamePasswordAuthenticationToken isValidInMemoryAdmin(String username,String password) {
		List<String> userRole = new ArrayList<String>();
		userRole.add("ROLE_ADMIN_READ_ONLY");
		Collection<? extends GrantedAuthority> authorities 
				= userRole.stream().map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());
		
		if ("admin".equalsIgnoreCase(username) && "admin@123".equalsIgnoreCase(password)){
	       return new UsernamePasswordAuthenticationToken
	              (username, null, authorities);
	    }else {
	    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(password);
			if ("admin".equalsIgnoreCase(username) && encoder.matches("adminenc@123",encodedPassword)) {
		       return new UsernamePasswordAuthenticationToken
		              (username, null, authorities);
		    }else {
		    	return new UsernamePasswordAuthenticationToken(username,password);
		    }
	    }
	}

	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
