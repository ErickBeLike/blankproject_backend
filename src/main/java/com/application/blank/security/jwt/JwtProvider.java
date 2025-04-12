package com.application.blank.security.jwt;

import com.application.blank.security.dto.JwtDTO;
import com.application.blank.security.entity.PrincipalUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration; // en segundos

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("La clave debe tener al menos 256 bits");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        List<String> roles = principalUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", principalUser.getUsername());
        claims.put("roles", roles);
        claims.put("iat", Instant.now().getEpochSecond());
        claims.put("exp", Instant.now().plusSeconds(expiration).getEpochSecond());

        return Jwts.builder()
                .claims(claims)
                .signWith(getSecretKey())
                .compact();
    }

    public String getNombreUsuarioFromToken(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(getSecretKey())
                .build();
        Jws<Claims> jws = parser.parseSignedClaims(token);
        return jws.getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    public String refreshToken(JwtDTO jwtDto) throws ParseException {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(jwtDto.getToken());
            return null; // Si no está expirado, no se refresca
        } catch (ExpiredJwtException e) {
            JwtParser parser = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build();
            Jws<Claims> jws = parser.parseSignedClaims(jwtDto.getToken());
            Claims claims = jws.getPayload();

            claims.put("iat", Instant.now().getEpochSecond());
            claims.put("exp", Instant.now().plusSeconds(expiration).getEpochSecond());

            return Jwts.builder()
                    .claims(claims)
                    .signWith(getSecretKey())
                    .compact();
        }
    }

    public long getExpirationFromToken(String token) {
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);
        return jws.getPayload().getExpiration().toInstant().getEpochSecond();
    }

}


