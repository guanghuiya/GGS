package com.meiqiu.controller;

import com.meiqiu.config.cache.LocalCache;
import com.meiqiu.config.cache.MemoryCache;
import com.meiqiu.config.cache.RemoteCache;
import com.meiqiu.service.CacheService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 缓存控制器
 * @Author sgh
 * @Date 2025/1/22
 * @Time 16:54
 */
@RestController
@RequestMapping("/cache")
@Api(tags = "缓存")
public class CacheController {

    /**
     * 三级缓存
     */
    public static void main(String[] args) {
        MemoryCache<String, String> memoryCache = new MemoryCache<>();
        LocalCache<String, String> localCache = new LocalCache<>(100); // 最大缓存100个元素
        RemoteCache<String, String> remoteCache = new RemoteCache<>();
        CacheService<String, String> cacheService = new CacheService<>(memoryCache, localCache, remoteCache);
        try {
            // 存入数据
            cacheService.putData("exampleKey", "exampleValue");
            // 获取数据
            String value = cacheService.getData("exampleKey", String.class);
            System.out.println(value);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
