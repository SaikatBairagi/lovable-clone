package com.apiorbit.lovableclone.security;

import com.apiorbit.lovableclone.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String jwtToken;

	public Key getSigningKey() {
		return Keys.hmacShaKeyFor(jwtToken.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(UserDetails user) {
        User authUser = (User)user;
		return Jwts.builder().setSubject(authUser.getId().toString()).claim("Email", authUser.getUsername())
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *10))
				.signWith(getSigningKey()).compact();

	}
	
	
	public Claims extractAllClaims(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(getSigningKey())
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
	
	
	public String extractUsername(String token) {
        return extractAllClaims(token).get("Email").toString();
    }

    public AuthenticatedUser extractUserRecord(String token) {
        Long userId =Long.parseLong(extractAllClaims(token).getSubject());
        String userEmail = extractAllClaims(token).get("Email").toString();
        return new AuthenticatedUser(userId, userEmail);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}
