package com.meiqiu.config;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.meiqiu.entity.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description redis分布式锁
 * @Author sgh
 * @Date 2025/2/19
 * @Time 17:48
 */
@Slf4j
@Component
public class RedisLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static Map<String, LockInfo> lockInfoMap = new ConcurrentHashMap<>();

    private static final Long SUCCESS = 1L;

    /**
     * 使用lua脚本更新redis锁的过期时间
     *
     * @param lockKey
     * @param value
     * @return 成功返回true, 失败返回false
     */
    public boolean renewal(String lockKey, String value, int expireTime) {
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Boolean.class);
        redisScript.setScriptText(luaScript);
        List<String> keys = new ArrayList<>();
        keys.add(lockKey);

        Object result = redisTemplate.execute(redisScript, keys, value, expireTime);
        log.info("更新redis锁的过期时间：{}", result);
        return (boolean) result;
    }

    /**
     * @param lockKey    锁
     * @param value      身份标识（保证锁不会被其他人释放）
     * @param expireTime 锁的过期时间（单位：秒）
     * @return 成功返回true, 失败返回false
     */
    public boolean lock(String lockKey, String value, long expireTime) {
        return redisTemplate.opsForValue().setIfAbsent(lockKey, value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * redisTemplate解锁
     *
     * @param key
     * @param value
     * @return 成功返回true, 失败返回false
     */
    public boolean unlock2(String key, String value) {
        Object currentValue = redisTemplate.opsForValue().get(key);
        boolean result = false;
        if (StringUtils.isNotEmpty(String.valueOf(currentValue)) && currentValue.equals(value)) {
            result = redisTemplate.opsForValue().getOperations().delete(key);
        }
        return result;
    }

    /**
     * 定时去检查redis锁的过期时间
     */
    @Scheduled(fixedRate = 5000L)
    @Async("redisExecutor")
    public void renewal() {
        long now = System.currentTimeMillis();
        for (Map.Entry<String, LockInfo> lockInfoEntry : lockInfoMap.entrySet()) {
            LockInfo lockInfo = lockInfoEntry.getValue();
            if (lockInfo.getRenewalTime() + lockInfo.getRenewalInterval() < now) {
                renewal(lockInfo.getKey(), lockInfo.getValue(), lockInfo.getExpireTime());
                lockInfo.setRenewalTime(now);
                log.info("lockInfo {}", JSONUtil.toJsonStr(lockInfo));
            }
        }
    }

    /**
     * 分布式锁设置单独线程池
     *
     * @return
     */
    @Bean("redisExecutor")
    public Executor redisExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("redis-renewal-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        return executor;
    }


}
