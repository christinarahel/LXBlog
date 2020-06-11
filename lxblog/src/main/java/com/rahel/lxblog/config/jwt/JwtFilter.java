package com.rahel.lxblog.config.jwt;

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

import com.rahel.lxblog.service.CustomUserDetailsService;

@Component
//@Log
public class JwtFilter extends GenericFilterBean {

	public static String AUTHORIZATION = "Authorization";
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("do filter");
		System.out.println("ffffiiiilllttttrrrrr");
		String token = getTokenFromRequest((HttpServletRequest) servletRequest);
		System.out.println("token = "+  token);
		if (token!=null && jwtProvider.validateToken(token)) {
			String userEmail = jwtProvider.getEmailFromToken(token);
			System.out.println("useremail = "+ userEmail);
			CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userEmail);
		    UsernamePasswordAuthenticationToken auth  = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		    SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(servletRequest, response);
	}
	
	 public String getTokenFromRequest(HttpServletRequest request) {
		//String bearer = 
			return request.getHeader(AUTHORIZATION);
	/*	System.out.println("bearer = "+ bearer);
		if((bearer)!=null&&(bearer.length()>0)&&(bearer.startsWith("Bearer"))) {
			return bearer.substring(7);
		}
		return null;*/
		
	}
 
}
