package com.meiqiu.service;

import com.meiqiu.config.cache.LocalCache;
import com.meiqiu.config.cache.MemoryCache;
import com.meiqiu.config.cache.RemoteCache;

/**
 * @Description 三级缓存
 * @Author sgh
 * @Date 2025/1/22
 * @Time 16:55
 */
public class CacheService<K, V> {

    // 一级缓存：内存缓存，通常使用内存来存储最频繁访问的数据，以提高读取效率。可以使用ConcurrentHashMap等并发集合来实现
    private final MemoryCache<K, V> memoryCache;

    // 二级缓存：本地缓存，使用本地文件或数据库来存储数据，适用于需要持久化的场景。可以使用Ehcache、Guava Cache等缓存库来实现
    private final LocalCache<K, V> localCache;

    // 三级缓存：远程缓存，更广泛的缓存机制，通常使用分布式缓存框架如，redis，memcached等，适用于分布式系统
    private final RemoteCache<K, V> remoteCache;

    public CacheService(MemoryCache<K, V> memoryCache, LocalCache<K, V> localCache, RemoteCache<K, V> remoteCache) {
        this.memoryCache = memoryCache;
        this.localCache = localCache;
        this.remoteCache = remoteCache;
    }

    public V getData(K key, Class<V> clazz) throws Exception {
        //一级缓存：内存
        V value = memoryCache.get(key);
        if (value == null) {
            //二次缓存：本地
            value = localCache.getIfPresent(key);
            if (value == null) {
                //三级缓存：远程
                value = remoteCache.get(key, clazz);
                if (value != null) {
                    //设置二级缓存
                    localCache.put(key, value);
                    //设置一级缓存
                    memoryCache.put(key, value);
                }
            } else {
                //设置一级缓存
                memoryCache.put(key, value);
            }
        }
        return value;
    }

    public void putData(K key, V value) throws Exception {
        memoryCache.put(key, value);
        localCache.put(key, value);
        remoteCache.put(key, value);
    }

}


