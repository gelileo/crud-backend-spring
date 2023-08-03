package com.gelileo.crud.services;

import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.entities.Token;
import com.gelileo.crud.helpers.JWTHelper;
import com.gelileo.crud.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AccessTokenService {
    @Value(value = "${security.jwt.token.secret.key:secret-key}")
    private String secretKey;

    @Value(value = "${security.jwt.token.exp.minutes:5}")
    private int expMinutes;

    private final TokenRepository tokenRepository;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /*
     * Revoke and create new
     * */
    public Token createToken(SystemUser user) {
        Token jwtToken = Token.builder()
                .user(user)
                .token(generateAccessToken(user))
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .expiryDate(Instant.now().plusSeconds(expMinutes * 60))
                .build();

        revokeForUser(user); // revoke all previous tokens

        Token accessToken = tokenRepository.save(jwtToken);
        return accessToken;
    }

    private void revokeForUser(SystemUser user) {
        var validUserTokens = tokenRepository.findAllValidAccessTokensByUser(user.getId(), Token.TokenType.BEARER);
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

//region JWT
    private String generateAccessToken(
            UserDetails userDetails) {
        Date now = new Date();
        return generateAccessToken(userDetails,
                new Date(now.getTime() + expMinutes * 60 * 1000));
    }

    private String generateAccessToken(
            UserDetails userDetails,
            Date exp
    ) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(JWTHelper.getSingerKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(JWTHelper.getSingerKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
//endregion

//region Refresh Token

//    public String generateRefreshToken(Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return generateRefreshToken(userDetails);
//    }
//
//    public String generateRefreshToken(UserDetails userDetails) {
//       return generateRefreshToken(userDetails.getUsername());
//    }

//    private String generateRefreshToken(String userName) {
//        Claims claims = Jwts.claims().setSubject(userName);
//
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXP_TIME))
//                .signWith(JWTHelper.getSingerKey(secretKey), SignatureAlgorithm.HS256)
//                .compact();
//    }
//endregion
}
