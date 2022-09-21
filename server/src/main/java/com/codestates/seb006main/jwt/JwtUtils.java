package com.codestates.seb006main.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    @Value("${jwt.access}")
    private String accessKey;
    @Value("${jwt.refresh}")
    private String refreshKey;


    public String createAccessToken(Long memberId, String email){

        String accessToken = JWT.create()
                .withSubject("hitch hiker access token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60)))
                .withClaim("id", memberId)
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(accessKey));
        accessToken = "Bearer " + accessToken;
        return accessToken;
    }

    public String createRefreshToken(Long memberId, String email){
        String refreshToken = JWT.create()
                .withSubject("hitch hiker refresh token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)))
                .withClaim("id", memberId)
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(refreshKey));
        refreshToken = "Bearer " + refreshToken;
        return refreshToken;
    }

    public boolean isTokenExpired(DecodedJWT jwt){
        Date exp = jwt.getExpiresAt();
        return exp.before(new Date());
    }

    public Map<String,Object> getClaimsFromToken(String token, String keys){
        String key = keys.equals("refresh")?refreshKey:accessKey;
        String email = JWT.require(Algorithm.HMAC512(key)).build().verify(token).getClaim("email").asString();
        Long id = JWT.require(Algorithm.HMAC512(key)).build().verify(token).getClaim("id").asLong();
        return Map.of("email",email,"id",id);
    }
}
