package com.example.authenticationmicroservice.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "mySuperSecretKeyForJwtEncryptionMustBeAtLeast32CharsLong123!";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(UUID id, String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id.toString())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.HOURS))) //token valid for 10 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }


    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public UUID extractId(String token) {
        String idString = (String) getClaims(token).get("id");
        return UUID.fromString(idString);
    }

    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
