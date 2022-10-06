package com.codestates.seb006main.config.security;

import com.codestates.seb006main.config.redis.RedisUtils;
import com.codestates.seb006main.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CustomLogoutHandler implements LogoutHandler {

    private final RedisUtils redisUtils;
    private final JwtUtils jwtUtils;

    public CustomLogoutHandler(RedisUtils redisUtils, JwtUtils jwtUtils) {
        this.redisUtils = redisUtils;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("access_hh");
        Long expire=jwtUtils.getExpire(token);
        redisUtils.setBlacklist(token,expire);
    }
}
