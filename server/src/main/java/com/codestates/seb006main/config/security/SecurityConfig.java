package com.codestates.seb006main.config.security;

import com.codestates.seb006main.config.redis.RedisUtils;
import com.codestates.seb006main.jwt.JwtUtils;
import com.codestates.seb006main.jwt.filter.JwtAuthenticationFilter;
import com.codestates.seb006main.jwt.filter.JwtAuthorizationFilter;
import com.codestates.seb006main.members.repository.MemberRepository;
import com.codestates.seb006main.oauth.CustomOAuth2SuccessHandler;
import com.codestates.seb006main.oauth.CustomOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Order(3)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final CustomOAuth2Service customOAuth2Service;
    private final RedisUtils redisUtils;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        http
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomDsl())
                .and()
                .authorizeRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST,"/api/members/login","/api/members").permitAll()
                        .antMatchers(HttpMethod.GET,"/login/oauth2/code/kakao","/api/members/email","/api/members/display-name","/websocket/**","/","/index.html").permitAll()
                        .antMatchers(HttpMethod.GET).access("hasRole('ROLE_MEMBER')")
                        .antMatchers(HttpMethod.POST).access("hasRole('ROLE_MEMBER')")
                        .antMatchers(HttpMethod.PATCH).access("hasRole('ROLE_MEMBER')")
                        .antMatchers(HttpMethod.DELETE).access("hasRole('ROLE_MEMBER')")
                        .anyRequest().permitAll());
        http
                .oauth2Login()
                .successHandler(customOAuth2SuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2Service);


        http
                .logout()
                .logoutUrl("/api/members/logout")
                .addLogoutHandler(customLogoutHandler);

        return http.build();
    }

    public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder
                    .addFilter(corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtUtils, redisUtils))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository, jwtUtils, redisUtils));
        }


    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Access_HH");
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
