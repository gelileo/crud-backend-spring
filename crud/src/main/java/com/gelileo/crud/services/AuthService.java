package com.gelileo.crud.services;

import com.gelileo.crud.exceptions.UserError;
import com.gelileo.crud.model.AuthRequest;
import com.gelileo.crud.model.AuthResults;
import com.gelileo.crud.model.RegisterRequest;
import com.gelileo.crud.entities.Role;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.entities.Token;
import com.gelileo.crud.exceptions.TokenRefreshException;
import com.gelileo.crud.repository.RoleRepository;
import com.gelileo.crud.repository.SystemUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
    private final RoleRepository roleRepository;

    public SystemUser register(RegisterRequest request) {

        Optional<Role> adminRole = roleRepository.findByName("ADMIN");
        if (adminRole.isEmpty()) {
            throw UserError.RoleMissing.exception("ADMIN role not found!");
        }

        SystemUser user = SystemUser.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .gender(SystemUser.Gender.MALE)
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(adminRole.get()))
                .build();
//        System.out.println("Password length : " + user.getPassword().length());
//        System.out.println(Hashing.sha256().hashString(request.getPassword(), Charset.defaultCharset()));
        userRepository.save(user);
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
