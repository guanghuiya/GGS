package com.meiqiu.config.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 一级缓存：内存缓存：常见的一级缓存包括静态变量、方法的局部变量及线程本地存储（ThreadLocal）。这些变量的访问速度很快，适合存储频繁访问的数据。
 * @Author sgh
 * @Date 2025/1/23
 * @Time 10:55
 */
public class MemoryCache<K, V> {
    private final Map<K, V> cache = new ConcurrentHashMap<>();

    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }
}
