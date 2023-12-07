package com.dhmi.ais.security.jwt;

import com.dhmi.ais.exception.CustomException;
import com.dhmi.ais.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${SECRET}")
    private String jwtSecret;

    @Value("${EXPIRATION}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtExpiration)).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Geçersiz JWT imzası. ", e.getMessage());
            throw new SignatureException("Geçersiz JWT imzası. " + e.getMessage(), e);
        } catch (MalformedJwtException e) {
            logger.error("Geçersiz JWT imzası. ", e.getMessage());
            throw new CustomException("Geçersiz JWT imzası. " + e.getMessage(), 403);
        } catch (ExpiredJwtException e) {
            logger.error("JWT süresi doldu. ", e.getMessage());
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "JWT süresi doldu. " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT desteklenmiyor. ", e.getMessage());
            throw new UnsupportedJwtException("JWT desteklenmiyor. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims boş. ", e.getMessage());
            throw new IllegalArgumentException("JWT claims boş. " + e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
