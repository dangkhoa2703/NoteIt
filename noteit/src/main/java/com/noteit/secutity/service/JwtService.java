package com.noteit.secutity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * Jwt service.
 */
@Service
public class JwtService {

    private static final String SECRET_KEY = "79787341765637766A463766376D6F304854413855434D486A614A4D61575A68";

    /**
     * Extract username string.
     *
     * @param token the token
     * @return the string
     */
//    extract username from token
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);

    }

    /**
     * Extract a claim from token.
     *
     * @param <T>            the claim type
     * @param token          the token
     * @param claimsResolver function to extract claim
     * @return the claim
     */
//    extract one claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate token string.
     *
     * @param userDetails the user details
     * @return the string
     */
//    generate a token with given userDetails
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate token string.
     *
     * @param extraClaims the extra claims
     * @param userDetails the user details
     * @return the string
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 24))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Decode the token and extract all claims from it.
     *
     * @param token the token
     * @return all claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if token is still valid.
     *
     * @param token       the token
     * @param userDetails the user details
     * @return the boolean
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Check if token is still valid.
     *
     * @param token       the token
     * @return the boolean
     */

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract expiration date.
     *
     * @param token       the token
     * @return expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Create a signing key for token.
     * @return the key
     */
    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
