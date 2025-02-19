package com.meiqiu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

/**
 * @Description redis 配置
 * @Author sgh
 * @Date 2025/1/21
 * @Time 17:20
 */
@Configuration
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) throws Exception {

        RedisTemplate template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        //创建序列化类
        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
        template.setKeySerializer(genericToStringSerializer);
        template.setValueSerializer(genericToStringSerializer);
        return template;
    }
}
