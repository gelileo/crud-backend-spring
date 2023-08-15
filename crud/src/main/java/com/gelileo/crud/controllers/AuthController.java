package com.gelileo.crud.controllers;

import com.gelileo.crud.entities.Role;
import com.gelileo.crud.model.AuthRequest;
import com.gelileo.crud.model.AuthResponse;
import com.gelileo.crud.model.AuthResults;
import com.gelileo.crud.model.RegisterRequest;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.entities.Token;
import com.gelileo.crud.repository.SystemUserRepository;
import com.gelileo.crud.services.AuthService;
import com.gelileo.crud.services.AccessTokenService;
import com.gelileo.crud.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request) {
        AuthResults result = authService.authenticate(request);

        ResponseCookie cookie = refreshTokenService.makeCookie(result.getRefreshToken());
        try {
            AuthResponse rsp = AuthResponse
                    .builder()
                    .token(result.getAccessToken().getToken())
                    .username(result.getUser().getUsername())
                    .roles(result.getUser().getRoles().stream().map(Role::getName).toList())
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(rsp);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            throw new RuntimeException("login failed");
        }

    }

    @PostMapping("/refreshtoken")
    ResponseEntity refreshToken(@CookieValue("refresh-token") String refreshToken) {
        Token newAccessToken = authService.refresh(refreshToken);
        return ResponseEntity.ok()
                .body(AuthResponse
                        .builder()
                        .token(newAccessToken.getToken())
                        .roles(newAccessToken.getUser().getRoles().stream().map(Role::getName).toList())
                        .username(newAccessToken.getUser().getUsername())
                        .build());
    }
}
