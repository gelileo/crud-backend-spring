package com.gelileo.crud.controllers;

import com.gelileo.crud.dto.AuthRequest;
import com.gelileo.crud.dto.AuthResponse;
import com.gelileo.crud.dto.RegisterRequest;
import com.gelileo.crud.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse>register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public @ResponseBody AuthResponse authenticate(
            @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, please login";
    }

}
