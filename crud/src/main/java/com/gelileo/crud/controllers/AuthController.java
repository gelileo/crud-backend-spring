package com.gelileo.crud.controllers;

import com.gelileo.crud.dto.AuthRequest;
import com.gelileo.crud.dto.AuthResponse;
import com.gelileo.crud.dto.AuthResults;
import com.gelileo.crud.dto.RegisterRequest;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.services.AuthService;
import com.gelileo.crud.services.AccessTokenService;
import com.gelileo.crud.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value(value = "${security.token.refresh.name:refresh-token}")
    private String cookieName;
    private final AuthService authService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<SystemUser>register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(
            @RequestBody AuthRequest request) {
        AuthResults result = authService.authenticate(request);

        ResponseCookie cookie = refreshTokenService.makeCookie(result.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(AuthResponse
                        .builder()
                        .token(result.getAccessToken().getToken())
                        .build());
    }

    @PostMapping("/refreshtoken")
    ResponseEntity refreshToken(@CookieValue("refresh-token") String refreshToken) {
        String newAccessToken = authService.refresh(refreshToken);
        return ResponseEntity.ok()
                .body(AuthResponse
                        .builder()
                        .token(newAccessToken)
                        .build());
    }
}
