package com.rahel.lxblog.config.jwt;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rahel.lxblog.entity.BlogUser;

//import io.jsonwebtoken.lang.Collections;

public class CustomUserDetails implements UserDetails{
 // to customize after ))))
	
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> grantedAuthorities;
	
	public static CustomUserDetails fromUserEntityCustomUserDetails(BlogUser blogUser) {
		CustomUserDetails c =new CustomUserDetails();
		c.email=blogUser.getEmail();
		c.password = blogUser.getPassword();
		c.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
		System.out.println(" CustomUserDetails,  fromUserEntityCustomUserDetails       "+blogUser);
		System.out.println(c.grantedAuthorities);
		System.out.println(" hello ");
	    return c;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	

}
