package com.rahel.lxblog.jwt;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	@Value("$(jwt.secret)")
	private String jwtSecret;

	public String generateToken(String email, Integer id) {
		java.util.Date date = java.util.Date
				.from(LocalDate.now().plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Claims claims = Jwts.claims().setSubject(email); 
		claims.put("id", id);
		return Jwts.builder().setClaims(claims).setExpiration(date).signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public boolean validateToken(String token) { 
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new JwtAuthenticationException("Invalid token");
		}

	}

	public String getEmailFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

}
