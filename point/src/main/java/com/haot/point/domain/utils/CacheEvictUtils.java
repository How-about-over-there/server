package com.haot.point.domain.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheEvictUtils {
    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 캐시 삭제: 특정 키 삭제
     *
     * @param cacheName 캐시 이름
     * @param key       캐시 키
     */
    public void evict(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
            log.info("캐시 삭제: cacheName={}, key={}", cacheName, key);
        }
    }

    /**
     * 캐시 삭제: userId를 기반으로 특정 캐시 삭제
     * userPointHistories 캐시에서 userId로 시작하는 모든 키 삭제
     *
     * @param userId 사용자 ID
     */
    public void evictUserPointHistoriesByUserId(String userId) {
        String pattern = "userPointHistories::" + userId + "*";
        Set<String> keysToEvict = getAllKeysForCache(pattern);

        keysToEvict.forEach(key -> {
            redisTemplate.delete(key); // RedisTemplate 을 통해 키 삭제
            log.info("캐시 삭제: cacheName=userPointHistories, key={}", key);
        });
    }

    /**
     * Redis 에서 특정 패턴에 해당하는 키 목록 가져오기
     *
     * @param pattern 키 패턴 (예: "userPointHistories:user123*")
     * @return 패턴에 해당하는 키 목록
     */
    private Set<String> getAllKeysForCache(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern); // 특정 패턴의 키 검색
        if (keys != null) {
            return keys;
        }
        return Set.of(); // 검색 결과가 없으면 빈 세트 반환
    }

    /**
     * 캐시 삭제: pointHistory 캐시에서 특정 historyId로 삭제
     *
     * @param historyId 포인트 히스토리 ID
     */
    public void evictPointHistory(String historyId) {
        evict("pointHistory", historyId);
    }

    /**
     * 캐시 삭제: point 캐시에서 특정 userId로 삭제
     *
     * @param userId 사용자 ID
     */
    public void evictPoint(String userId) {
        evict("point", userId);
    }
}
