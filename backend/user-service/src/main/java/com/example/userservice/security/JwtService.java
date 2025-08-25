package com.example.userservice.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    private final Key key;
    private final long accessMillis;
    private final long refreshMillis;


    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-ttl}") Duration accessTtl,
            @Value("${security.jwt.refresh-token-ttl}") Duration refreshTtl
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessMillis = accessTtl.toMillis();
        this.refreshMillis = refreshTtl.toMillis();
    }


    public String generateAccessToken(String subject, Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateRefreshToken(String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token, String subject) {
        try {
            return subject.equals(extractSubject(token)) && !isExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }


    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return resolver.apply(claims);
    }


    public boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


    public long getAccessMillis() { return accessMillis; }
    public long getRefreshMillis() { return refreshMillis; }
}