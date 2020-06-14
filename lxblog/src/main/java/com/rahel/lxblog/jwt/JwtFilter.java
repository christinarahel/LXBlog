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
//@Log
public class JwtFilter extends GenericFilterBean {

	public static String AUTHORIZATION = "Authorization";

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("do filter");
		// System.out.println("ffffiiiilllttttrrrrr");
		String token = getTokenFromRequest((HttpServletRequest) request);
		System.out.println("token = " + token);
		if (token != null && jwtProvider.validateToken(token)) {
			String userEmail = jwtProvider.getEmailFromToken(token);
			System.out.println("useremail = " + userEmail);
			JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(userEmail);
		    System.out.println(jwtUserDetails);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwtUserDetails, null,
					jwtUserDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			System.out.println(auth);
		}
		chain.doFilter(request, response);
	}

	public String getTokenFromRequest(HttpServletRequest request) {
		String bearer = request.getHeader(AUTHORIZATION);
		System.out.println("bearer = "+ bearer);
		if ((bearer != null) && (bearer.length() > 0)) {
			if (bearer.startsWith("Bearer")) {
				return bearer.substring(7);
			} else
				return bearer;
		}
		return null;
	}
}
