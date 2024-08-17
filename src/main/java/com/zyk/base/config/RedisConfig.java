package com.zyk.base.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Configuration
public class RedisConfig {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 设置连接工厂 SpringBoot默认使用Lettuce的redis连接实现 Lettuce是基于Netty实现的非阻塞I/O(异步I/O)
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置key序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置value序列化器  也可以使用fastJson
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        return redisTemplate;
    }

    @Bean
    public JsonDeserializer<Timestamp> customTimestampDeserializer(){
        return new JsonDeserializer<Timestamp>() {
            @Override
            public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                String dateString = jsonParser.getText();
                try {
                    return new Timestamp(dateFormat.parse(dateString).getTime());
                } catch (ParseException e) {
                    throw new IOException("Cannot parse date string: " + dateString, e);
                }
            }
        };
    }

}
