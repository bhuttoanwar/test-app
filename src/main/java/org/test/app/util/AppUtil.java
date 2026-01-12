package org.test.app.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.test.app.service.ConfigPropertyQueryService;

import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class AppUtil {

	@Autowired
	ConfigPropertyQueryService configProps;

	public String generateToken(String username, String role) {
		Instant now = Instant.now();
		Instant expiry = now.plusSeconds(configProps.getJwtTtlSeconds());
		return Jwts.builder().subject(username).claims(Map.of("role", role)).issuedAt(Date.from(now))
				.expiration(Date.from(expiry)).signWith(getSigningKey()).compact();
	}

	private Key getSigningKey() {
		byte[] keyBytes = Base64.getDecoder().decode(configProps.getJwtSecret());
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@SuppressWarnings("deprecation")
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}
	
	
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public String generateSecureKey() {
		byte[] key = new byte[32]; // 256-bit
		new SecureRandom().nextBytes(key);
		return Base64.getEncoder().encodeToString(key);
	}

	
	
	
}
