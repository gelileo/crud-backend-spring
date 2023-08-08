package com.gelileo.crud.services;

import com.gelileo.crud.constants.Role;
import com.gelileo.crud.dto.AuthRequest;
import com.gelileo.crud.dto.AuthResults;
import com.gelileo.crud.dto.RegisterRequest;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.entities.Token;
import com.gelileo.crud.exceptions.TokenRefreshException;
import com.gelileo.crud.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final SystemUserRepository userRepository;
//    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    public SystemUser register(RegisterRequest request) {
        SystemUser user = SystemUser.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .gender(SystemUser.Gender.MALE)
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.ADMIN))
                .build();
        System.out.println("Password length : " + user.getPassword().length());
        user = userRepository.save(user);
        return user;
    }

    public AuthResults authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        SystemUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        // Previous tokens get revoked when creating new
        Token accessToken = accessTokenService.createToken(user);
        Token refreshToken = refreshTokenService.createToken(user);

        return AuthResults
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }

    public Token refresh(String refreshToken) {
       return refreshTokenService
                .findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(Token::getUser)
                .map(accessTokenService::createToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken,
                        "Refresh token is not in database!"));
    }

}
