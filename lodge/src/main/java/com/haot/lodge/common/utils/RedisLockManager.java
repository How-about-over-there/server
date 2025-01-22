package com.haot.lodge.common.utils;


import com.haot.lodge.common.exception.ErrorCode;
import com.haot.lodge.common.exception.LodgeException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockManager {

    private final RedissonClient redissonClient;

    /**
     * Redis Lock 획득
     * @param keys 락을 걸고자 하는 리소스의 식별자 리스트
     * @param prefix 락 키에 사용할 접두사 (Redis 키 네이밍)
     * @param waitTime 락을 획득하기 위해 대기할 최대 시간 (초)
     * @param leaseTime 락이 유지될 시간 (초)
     * @return 획득한 Redis 락 객체 목록
     * @throws LodgeException  락을 획득하지 못하거나, 획득 중 인터럽트가 발생한 경우
     */
    public List<RLock> acquireLocks(List<String> keys, String prefix, long waitTime, long leaseTime) {
        List<RLock> locks = new ArrayList<>();
        for (String key : keys) {
            RLock lock = redissonClient.getLock(prefix + key);
            try {
                if (!lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                    throw new LodgeException(ErrorCode.REDIS_LOCK_ACQUISITION_FAILED);
                }
                locks.add(lock);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new LodgeException(ErrorCode.REDIS_LOCK_INTERRUPTED);
            }
        }
        return locks;
    }

    /**
     * Redis Lock 해제
     * @param locks 해제할 Redis 락 객체 목록
     */
    public void releaseLocks(List<RLock> locks) {
        for (RLock lock : locks) {
            if (lock.isHeldByCurrentThread()) {
                try {
                    lock.unlock();
                } catch (IllegalStateException e) {
                    throw new LodgeException(ErrorCode.REDIS_LOCK_RELEASE_FAILED);
                }
            }
        }
    }
}
