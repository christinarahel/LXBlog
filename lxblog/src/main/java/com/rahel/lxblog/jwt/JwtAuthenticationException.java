package com.rahel.lxblog.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

	public JwtAuthenticationException(String msg) {
		super(msg);
	}

}
