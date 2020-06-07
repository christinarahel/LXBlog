package com.rahel.lxblog.config.jwt;


import java.time.LocalDate;
import java.time.ZoneId;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;

@Component
@Log
public class JwtProvider {
		
    @Value("$(jwt.secret)")
	private String jwtSecret;
 
	public String generateToken(String email) {
		java.util.Date date = java.util.Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
		return Jwts.builder()
				.setSubject(email)    // hear I can add another subject
				.setExpiration(date)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	
	public boolean validateToken(String token) {  //I need to check all the exceptions 
		try {
		Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
		return true;
		}
		catch (Exception e) {
			System.out.println("invalid token");
	    }
		return false;
	}
	public String getEmailFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
}
