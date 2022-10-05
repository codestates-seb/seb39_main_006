package com.codestates.seb006main.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.config.redis.RedisUtils;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.jwt.JwtUtils;
import com.codestates.seb006main.members.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,JwtUtils jwtUtils,RedisUtils redisUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils=jwtUtils;
        this.redisUtils=redisUtils;
        this.setFilterProcessesUrl("/api/members/login");
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper om = new ObjectMapper();
            Member member = om.readValue(request.getInputStream(), Member.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAuthentication");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        if(!principalDetails.getMember().getMemberStatus().name().equals("ACTIVE")){
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_ACTIVE);
        }
        Member member = principalDetails.getMember();
        Long memberId = member.getMemberId();
        String email = member.getEmail();
        String accessToken = jwtUtils.createAccessToken(memberId,email,member.getDisplayName());
        String refreshToken = jwtUtils.createRefreshToken(memberId,email);
        redisUtils.setRefreshToken(memberId,refreshToken);
        response.addHeader("Access_HH", accessToken);
        chain.doFilter(request,response);
    }


}
