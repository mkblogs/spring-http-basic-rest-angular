package com.tech.mkblogs.security.db;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tech.mkblogs.model.Authorities;
import com.tech.mkblogs.model.User;
import com.tech.mkblogs.user.repository.UserRepository;

@Component
public class DBAuthProvider implements AuthenticationProvider{

	@Autowired
	UserRepository repository;
	
	@Override
	@Transactional
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal() + "";
	    String password = authentication.getCredentials() + "";
	    
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    User user = repository.findByLoginName(username);
	    if (user == null) {
	        throw new BadCredentialsException("1000");
	    }
	    if(user.getEncrypted() != null && user.getEncrypted()) {
	    	if (!encoder.matches(password, user.getPassword())) {
	 	        throw new BadCredentialsException("1000");
	 	    }
	    }else {
	    	if(!password.equalsIgnoreCase(user.getPassword())) {
	    		throw new BadCredentialsException("1000");
	    	}
	    }
	    if (user.getEnabled() != null && user.getEnabled()) {
	        throw new DisabledException("1001");
	    }
	    Set<Authorities> userRights = user.getAuthorities();
	    return new UsernamePasswordAuthenticationToken(username, null, userRights.stream().map(x -> new SimpleGrantedAuthority(x.getAuthority())).collect(Collectors.toList()));
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	
}
