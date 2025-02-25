package com.example.ecommerce_app.Services.JWT;

import com.example.ecommerce_app.Dto.User.UserInfoDetails;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomAuthorizationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String Email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(Email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (ExpiredJwtException ex) {
            System.out.println("Token has expired: ");
            throw new CustomAuthorizationException("JWT Expired");
        } catch (JwtException ex) { // Catch all JWT-related exceptions
            System.out.println("Invalid JWT: " + ex.getMessage());
            throw new CustomAuthorizationException("Invalid JWT");
        } catch (Exception ex) { // Catch any other unexpected exceptions
            System.out.println("Unexpected error: " + ex.getMessage());
            throw new CustomAuthorizationException("Internal Server Error");
        }
}
}
