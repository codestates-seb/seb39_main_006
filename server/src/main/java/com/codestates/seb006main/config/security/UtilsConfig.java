package com.codestates.seb006main.config.security;

import com.codestates.seb006main.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(2)
public class UtilsConfig {
    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }
}
