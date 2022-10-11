package com.codestates.seb006main.oauth;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.config.redis.RedisUtils;
import com.codestates.seb006main.jwt.JwtUtils;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.repository.MemberRepository;
import com.google.gson.Gson;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JwtUtils jwtUtils;
    private MemberRepository memberRepository;
    private RedisUtils redisUtils;
    private Gson gson;


    public CustomOAuth2SuccessHandler(JwtUtils jwtUtils, MemberRepository memberRepository, RedisUtils redisUtils) {
        this.jwtUtils = jwtUtils;
        this.memberRepository = memberRepository;
        this.redisUtils = redisUtils;
        this.gson= new Gson();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        String accessToken = jwtUtils.createAccessToken(member.getMemberId(), member.getEmail(),member.getDisplayName());
        String refreshToken = jwtUtils.createRefreshToken(member.getMemberId(), member.getEmail());
        redisUtils.setRefreshToken(member.getMemberId(),refreshToken);
        getRedirectStrategy().sendRedirect(request,response,"https://hitch-hiker.kr/oauth2/redirect?token="+accessToken);
    }
}
