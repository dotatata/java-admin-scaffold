package com.jiayun.erp.wms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 设置连接工厂 SpringBoot默认使用Lettuce的redis连接实现 Lettuce是基于Netty实现的非阻塞I/O(异步I/O)
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置key序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置value序列化器  也可以使用fastJson
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));

        return redisTemplate;
    }

}
