package com.gelileo.crud.helpers;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JWTHelper {

//    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public static String getJWTFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public static Key getSingerKey(String secretKey) {
//        TestKey key = new TestKey();
//        String secret = key.getSecretKey();
//        byte[] bytes = Decoders.BASE64.decode("OWYyZmQ4ZTAtNDU0NC0xMWVjLTgxZDMtMDI0MmFjMTMwMDA=");
//        byte[] bytes = Decoders.BASE64URL.decode(secretKey);
        byte[] bytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(bytes);

    }

}
