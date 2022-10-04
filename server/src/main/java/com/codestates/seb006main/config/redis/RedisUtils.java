package com.codestates.seb006main.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String,Object> redisTemplate;

    public String getRefreshToken(Long memberId){
        return (String) redisTemplate.opsForHash().get(String.valueOf(memberId),"refresh");
    }

    public void setRefreshToken(Long memberId, String refreshToken){
        HashOperations<String,String,String> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(String.valueOf(memberId),"refresh",refreshToken);
    }


    public void deleteRefreshToken(Long memberId){
        redisTemplate.opsForHash().delete(String.valueOf(memberId),"refresh");
    }

}
