package com.jtrack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/loginHist").permitAll()
				.antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT, "/users/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/users/*").hasRole("ADMIN")
				.anyRequest().authenticated();
	}

	
}
