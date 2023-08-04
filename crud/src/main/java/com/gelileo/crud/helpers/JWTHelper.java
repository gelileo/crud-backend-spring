package com.gelileo.crud.helpers;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JWTHelper {
    public static String getJWTFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public static Key getSingerKey(String secretKey) {
        //scramble with base64
    //  byte[] bytes = Decoders.BASE64.decode(Encoders.BASE64.encode(secretKey.getBytes()))
        byte[] bytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(bytes);
    }

}
