package com.jsp.springboot.film.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}") // ✅ Load secret dynamically
    private String secret;

    @Value("${jwt.expiration}") // ✅ Load expiration dynamically
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // ✅ Generate JWT Token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority()) // ✅ Store role
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // ✅ Uses configured expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // ✅ Uses dynamically loaded key
                .compact();
    }

    // ✅ Extract username from JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Extract claims from JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // ✅ Uses dynamically loaded key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Check if token is expired
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // ✅ Validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // ✅ Refresh Token (Extend Expiration)
    public String refreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return Jwts.builder()
                    .setSubject(claims.getSubject())
                    .claim("role", claims.get("role"))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // ✅ Uses configured expiration time
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (ExpiredJwtException e) {
            System.out.println("⚠️ Cannot refresh expired token.");
            return null;
        }
    }
}
