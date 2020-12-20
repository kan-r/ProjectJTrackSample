package com.jtrack.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jtrack.dto.UserResp;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
    private UserService userService;
    
    @Autowired
    private LoginHistService loginHistService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        
    	/*
    	 * Note:
		 * userId will not be null (validated by the login)
		 * password must be same as userId (case sensitive)
		 */
    	
    	boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
                
        UserResp user = userService.getUser(userId.toUpperCase());
        
        String pword = "";
        if(user != null) {
        	pword = user.getPword();
        	loginHistService.addLoginHist(user.getUserId());
        }
       
        return new org.springframework.security.core.userdetails.User(
        				userId, 
        				passwordEncoder.encode(pword), 
                        enabled, 
                        accountNonExpired, 
                        credentialsNonExpired, 
                        accountNonLocked,
                        getAuthorities(userId));
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(String userId) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(userId));
        return authList;
    }

    private List<String> getRoles(String userId) {

        List<String> roles = new ArrayList<String>();

        if(userId != null && !userId.equals("")) {
        	if(userId.equalsIgnoreCase("ADMIN")) {
        		roles.add("ROLE_USER");
                roles.add("ROLE_ADMIN");
        	}else {
        		roles.add("ROLE_USER");
        	}
        }     
        
        return roles;
    }

    private static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
