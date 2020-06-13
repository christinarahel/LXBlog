package com.rahel.lxblog.service;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.jwt.JwtUserDetails;

@Component
@Transactional
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private BlogUserService blogUserService;

	@Override
	public JwtUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		BlogUser user = blogUserService.findByEmail(userName).get();
		if(user == null) {
			throw new UsernameNotFoundException("User with e-mail:" + userName+" is not found");
		}
		return JwtUserDetails.fromUserEntityToJwtUserDetails(user);
	}

}
