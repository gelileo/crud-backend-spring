package com.gelileo.crud.services;

import com.gelileo.crud.helpers.JWTHelper;
import com.gelileo.crud.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt = JWTHelper.getJWTFromRequest(request);
        if (jwt != null) {
            var storedToken = tokenRepository.findByToken(jwt).orElse(null);
            if (storedToken != null) {
                // invalidate the token
                storedToken.setExpired(true);
                storedToken.setRevoked(true);
                tokenRepository.save(storedToken);
                // log out the uer
                SecurityContextHolder.clearContext();
            }
        }
    }


}
