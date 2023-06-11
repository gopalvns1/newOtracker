package com.onpassive.otracker.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	
	String secretKey="onpassive";
	
	public String generateToken(String email) {
		Map<String, Object> claims=new HashMap<>();
		return createToken(claims,email);
		
	}

	private String createToken(Map<String, Object> claims, String subject) {
		
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() +1000*60*60*10))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username=extractUserName(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
		
	}
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims=exractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims exractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		
	}

	public String extractUserName(String token) {
		// TODO Auto-generated method stub
		 return extractClaim(token, Claims::getSubject);
	}

}
