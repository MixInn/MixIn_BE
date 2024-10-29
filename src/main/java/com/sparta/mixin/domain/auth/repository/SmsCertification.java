package com.sparta.mixin.domain.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SmsCertification {
    private final String PREFIX = "sms:";
    private final int LIMIT_TIME = 3 * 60;

    private final StringRedisTemplate stringRedisTemplate;

    public void createSmsCertification(String phoneNumber, String certificationCode) {
        stringRedisTemplate.opsForValue().set(PREFIX + phoneNumber, certificationCode, Duration.ofSeconds(LIMIT_TIME));
    }

    public String getSmsCertification(String phoneNumber) {
        return stringRedisTemplate.opsForValue().get(PREFIX + phoneNumber);
    }

    public boolean hasKey(String phoneNumber) {
        return stringRedisTemplate.hasKey(PREFIX + phoneNumber);
    }

    public void deleteSmsCertification(String phoneNumber) {
        stringRedisTemplate.delete(PREFIX + phoneNumber);
    }
}
