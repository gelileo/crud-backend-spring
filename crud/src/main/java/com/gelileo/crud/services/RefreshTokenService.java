package com.gelileo.crud.services;

import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.entities.Token;
import com.gelileo.crud.exceptions.TokenRefreshException;
import com.gelileo.crud.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value(value = "${security.token.refresh.exp.hour:24}")
    private int expHours;

//    private final SystemUserRepository userRepository;
    private final TokenRepository tokenRepository;

    private static final String COOKIE_PATH = "/api/v1/auth";

    /*
     * Revoke and create new
     * */
    public Token createToken(SystemUser user) {
        Token refreshToken = Token.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .tokenType(Token.TokenType.REFRESH)
                .expired(false)
                .revoked(false)
                .expiryDate(Instant.now().plusSeconds(expHours * 60 * 60))
                .build();

        revokeForUser(user); // delete previous tokens in db

        refreshToken = tokenRepository.save(refreshToken);

        return refreshToken;
    }

    private void revokeForUser(SystemUser user) {
        var previousTokens = tokenRepository.findAllByUserAndTokenType(user, Token.TokenType.REFRESH);
        if (previousTokens.isEmpty())
            return;

        previousTokens.forEach(token -> {
           tokenRepository.delete(token);
        });
    }

    public ResponseCookie makeCookie(Token refreshToken) {
        if (refreshToken.getTokenType() != Token.TokenType.REFRESH) {
            throw new RuntimeException("Unexpected refresh token");
        }
        ResponseCookie cookie = generateCookie("refresh-token",
                refreshToken.getToken(),
                COOKIE_PATH,
                (long) (expHours * 60 * 60));
        return cookie;
    }

    public Optional<Token> findByToken(String refreshToken) {
        return tokenRepository.findByToken(refreshToken);
    }

    public Token verifyExpiration(Token token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new login request");
        }

        return token;
    }
    private ResponseCookie generateCookie(String name,
                                          String value,
                                          String path,
                                          Long maxAgeSeconds) {
        ResponseCookie cookie = ResponseCookie
                .from(name, value)
                .path(path)
                .maxAge(maxAgeSeconds)
                .httpOnly(true)
                .build();
        return cookie;
    }
}
