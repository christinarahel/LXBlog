package com.rahel.lxblog.service;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.config.jwt.CustomUserDetails;


@Component
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private BlogUserService blogUserService;
	
	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BlogUser user = blogUserService.findByEmail(username);
        
        System.out.println(" CustomUserDetailsService,  loadUserByUsername(String username)       "+user);
		
		return CustomUserDetails.fromUserEntityCustomUserDetails(user);
		
	}

	
}
