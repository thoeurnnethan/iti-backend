package com.iti.thesis.helicopter.thesis.security.jwt;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.iti.thesis.helicopter.thesis.core.collection.MData;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "s3cr3tK3y1234567";
	private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7 day
	private final long EXPIRATION_TIME_REFRESH_TOKEN = 1000 * 60 * 60 * 24 * 30; // 30 Days

	public String generateToken(String username, MData claims) {
		return createToken(claims, username, EXPIRATION_TIME);
	}
	
	public String generateRefreshToken(String username) {
		MData claims = new MData();
		return createToken(claims, username, EXPIRATION_TIME_REFRESH_TOKEN);
	}

	private String createToken(MData claims, String subject, long expirationTime) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public boolean validateToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username) && !isTokenExpired(token));
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
}
