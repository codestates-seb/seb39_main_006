package com.codestates.seb006main.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.config.redis.RedisUtils;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.jwt.JwtUtils;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private MemberRepository memberRepository;
    private JwtUtils jwtUtils;
    private RedisUtils redisUtils;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtUtils jwtUtils, RedisUtils redisUtils) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청 됨.");
        String accessToken = request.getHeader("Access_HH");

        if(accessToken == null || !accessToken.startsWith("Bearer") ){
            chain.doFilter(request,response);
        }else if(redisUtils.chkBlacklist(accessToken)){
            throw new AuthenticationException("강탈당한 토큰");
        }else if (jwtUtils.isTokenExpired(JWT.decode(accessToken.replace("Bearer ", "")))){
            accessToken=accessToken.replace("Bearer ", "");
            Map<String,Object> memberInfoMap = jwtUtils.getClaimsFromToken(accessToken,"accessKey");
            Long memberId=(Long)memberInfoMap.get("id");
            String refreshToken = redisUtils.getRefreshToken(memberId);
            if(refreshToken == null || !refreshToken.startsWith("Bearer")){
            }else{
                refreshToken = refreshToken.replace("Bearer ", "");
                DecodedJWT jwt = JWT.decode(refreshToken);
                if(jwtUtils.isTokenExpired(jwt)){
                    throw new BusinessLogicException(ExceptionCode.TOKEN_EXPIRED);
                }else{
                    Map<String,Object> map = jwtUtils.getClaimsFromToken(refreshToken,"refresh");
                    Member memberEntity = memberRepository.findByEmail((String) map.get("email")).orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
                    String access = jwtUtils.createAccessToken((Long)map.get("id") ,(String) map.get("email"),memberEntity.getDisplayName());
                    response.setHeader("Access_HH",access);
                    PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                            principalDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            chain.doFilter(request,response);
        }else{
            accessToken=accessToken.replace("Bearer ", "");
            Map<String,Object> map = jwtUtils.getClaimsFromToken(accessToken,"access");

            Member memberEntity = memberRepository.findByEmail((String) map.get("email")).orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
            PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                    principalDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request,response);
        }
    }
}
