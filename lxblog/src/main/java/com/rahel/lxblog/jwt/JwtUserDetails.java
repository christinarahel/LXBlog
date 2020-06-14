package com.rahel.lxblog.jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rahel.lxblog.entity.BlogUser;

public class JwtUserDetails implements UserDetails {

	private final Integer id;
	private final String email;
	private final String password;
	private Collection<? extends GrantedAuthority> grantedAuthorities;

	public JwtUserDetails(Integer id, String email, String password,
			Collection<? extends GrantedAuthority> grantedAuthorities) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.grantedAuthorities = grantedAuthorities;
	}

	public static JwtUserDetails fromUserEntityToJwtUserDetails(BlogUser blogUser) {
		List<SimpleGrantedAuthority> grantedAuthorities = Collections
				.singletonList(new SimpleGrantedAuthority(blogUser.getRole_name()));
		JwtUserDetails userDetails = new JwtUserDetails(blogUser.getId(), blogUser.getEmail(), blogUser.getPassword(),
				grantedAuthorities);
		return userDetails;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Integer getId() {
		return id;
	}

}
