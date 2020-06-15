package com.tech.mkblogs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	AccountAuthenticateManager manager;
   
  // Securing the urls and allowing role-based access to these urls.
  @Override
  protected void configure(HttpSecurity http) throws Exception {
	  http
      .cors().and()
      .authorizeRequests()
      .antMatchers("/api/token").authenticated()
      .antMatchers("/api/admin/**").hasRole("ADMIN")
      .antMatchers("/api/user/**").hasAnyRole("USER","ADMIN")     
      .and()
      .httpBasic();
      sessionCode(http);
      http.csrf().disable();
  }
  
  private void sessionCode(HttpSecurity http) throws Exception {
	  http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
  }

  @Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return manager;
	}
  
  @Bean
  public SessionRegistry sessionRegistry() {
      return new SessionRegistryImpl();
  }

}
