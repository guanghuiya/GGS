package com.meiqiu.config.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @Description 二级缓存：本地缓存 （使用Guava Cache作为示例）
 * 通常是指会话范围内的缓存，但作用范围也可以是SessionFactory，跨多个Session。它用于存储一些不常变化的数据，以降低数据库访问次数。
 * 数据存储在内存中，可以在会话中共享。二级缓存的好处是可以减轻对数据库的压力。
 * @Author sgh
 * @Date 2025/1/23
 * @Time 10:55
 */
public class LocalCache<K, V> {
    private final Cache<K, V> cache;

    public LocalCache(long maxSize) {
        cache = CacheBuilder.newBuilder().maximumSize(maxSize).build();
    }

    public V getIfPresent(K key) {
        return cache.getIfPresent(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }
}
