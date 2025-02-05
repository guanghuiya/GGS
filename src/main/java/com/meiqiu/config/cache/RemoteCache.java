package com.meiqiu.config.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meiqiu.config.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description 三级缓存：远程缓存（使用Redis作为示例）
 * 是指更广泛的缓存机制，通常使用分布式缓存框架如Redis、Memcached等实现。三级缓存的访问速度较慢，但存储容量更大，主要用于跨用户、跨会话的数据共享。
 * 三级缓存适用于大多数高并发和分布式系统的开发场景。
 * @Author sgh
 * @Date 2025/1/23
 * @Time 10:55
 */
public class RemoteCache<K, V> {

    @Autowired
    private RedisCacheService redisCacheService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public V get(K key, Class<V> clazz) throws Exception {
        String valueJson = redisCacheService.get(key.toString());
        if (valueJson != null) {
            return objectMapper.readValue(valueJson, clazz);
        }
        return null;
    }

    public void put(K key, V value) throws Exception {
        String valueJson = objectMapper.writeValueAsString(value);
        redisCacheService.set(key.toString(), valueJson);
    }
}
