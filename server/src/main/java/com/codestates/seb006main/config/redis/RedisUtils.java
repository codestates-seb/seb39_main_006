package com.codestates.seb006main.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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

    public void setBlacklist(String accessToken, Long exp){
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        if(exp>0){
            operations.set("blk_"+accessToken,"logout",exp, TimeUnit.MILLISECONDS);
        }
    }

    public boolean chkBlacklist(String accessToken){
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        String value=(String)operations.get("blk_"+accessToken);
        System.out.println(value);
        if(value==null||value.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

}
