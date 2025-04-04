package com.io.fute.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.io.fute.entities.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${io.fute.password.generate.token}")
    private String secret;

    public String generateToken(AppUser user){
        try{
            var algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create().withIssuer("Futeio")
                    .withSubject(user.getId().toString())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);

            return token;

        }catch (JWTCreationException e){
            throw new RuntimeException("Error generating JWT token ", e);
        }
    }

    public String getSubject(String tokenJWT){
        try {
            var algorithm = Algorithm.HMAC256(secret);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer("Futeio")
                    .build();

            return jwtVerifier.verify(tokenJWT).getSubject();

        }catch (JWTVerificationException ex){
            throw new RuntimeException("Invalid or expired JWT token");
        }
    }
    private Instant expirationDate(){
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00"));
    }
}
