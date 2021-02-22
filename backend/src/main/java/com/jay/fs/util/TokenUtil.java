package com.jay.fs.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class TokenUtil {

    public static String generateToken(Long userId, String username, String password) throws Exception {
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        String token = JWT.create().withAudience(String.valueOf(userId))
                .withClaim("timestamp", System.currentTimeMillis())
                .sign(Algorithm.HMAC256(password));
        return token;
    }

    public static Long getUserId(String token){
        if(token == null) return null;
        String value = JWT.decode(token).getAudience().get(0);
        return value == null ? null : Long.valueOf(value);
    }
}
