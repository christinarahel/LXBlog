package com.rahel.lxblog.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtFilter extends GenericFilterBean {

	public static String AUTHORIZATION = "Authorization";

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = getTokenFromRequest((HttpServletRequest) request);
		if (token != null && jwtProvider.validateToken(token)) {
			String userEmail = jwtProvider.getEmailFromToken(token);
			JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(userEmail);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwtUserDetails, null,
					jwtUserDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(request, response);
	}

	public String getTokenFromRequest(HttpServletRequest request) {
		String bearer = request.getHeader(AUTHORIZATION);
		if ((bearer != null) && (bearer.length() > 0)) {
			if (bearer.startsWith("Bearer")) {
				return bearer.substring(7);
			} else
				return bearer;
		}
		return null;
	}
}
