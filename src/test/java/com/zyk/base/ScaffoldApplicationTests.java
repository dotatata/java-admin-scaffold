package com.zyk.base;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zyk.base.entity.Role;
import com.zyk.base.entity.User;
import com.zyk.base.mapper.UserMapper;
import com.zyk.base.util.CurrentThread;
import com.zyk.base.util.JwtUtil;
import com.zyk.base.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ScaffoldApplicationTests {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserMapper userMapper;
    @Autowired
    JsonDeserializer<Timestamp>  customTimestampDeserializer;


    @Test
    void contextLoads() {
    }

    @Test
    void redisTest(){
        User user = userMapper.getUserWithRolesByPhone("13888888888");

        String userCachedKey = CurrentThread.authUserCacheKey(user.getId());
        //redisUtil.set(userCachedKey, user);


        long startTime = System.currentTimeMillis();
        System.out.println("start time: " + startTime);

        // 反序列化缓存的User
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Timestamp.class, customTimestampDeserializer);
        objectMapper.registerModule(module);

        User cachedUser = objectMapper.convertValue(redisUtil.get(userCachedKey), User.class);
        System.out.println("cachedUser: " + cachedUser);
        System.out.println("userId: " + cachedUser.getId());
        System.out.println("name: " + cachedUser.getName());
        System.out.println("title: " + cachedUser.getTitle());
        System.out.println("phone: " + cachedUser.getPhone());
        System.out.println("createDate: " + cachedUser.getCreateDate());
        System.out.println("updateDate: " + cachedUser.getUpdateDate());
        System.out.println("roles: " + cachedUser.getRoles());
        System.out.println("roleIds: " + Arrays.toString(cachedUser.getRoleIds()));


        long endTime = System.currentTimeMillis();
        System.out.println("end time: " + endTime);

        System.out.println("elapsed: " + (endTime - startTime));

    }

    @Test
    void jwtTest(){
        System.out.println("start time: " + System.currentTimeMillis());

        User user = userMapper.getUserWithRolesByPhone("13888888888");


        Map<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("phone", user.getPhone());
        map.put("name", user.getName());
        map.put("roles", user.getRoles().stream().map(Role::getRoleKey).toArray(String[]::new));
        //map.put("permissions", user.getName());

        String token = JwtUtil.createTokenByMap(map);
        System.out.println("JWT token: " + token);


        System.out.println("==========decode token==========");
        DecodedJWT decodedToken = JwtUtil.getDecodedToken(token);
        System.out.println("decodedToken: " + decodedToken);
        System.out.println("getHeader: " + decodedToken.getHeader());
        System.out.println("getPayload: " + decodedToken.getPayload());
        System.out.println("getToken: " + decodedToken.getToken());
        System.out.println("getSignature: " + decodedToken.getSignature());
        System.out.println("claim 'uid': " + decodedToken.getClaim("uid"));
        System.out.println("claim 'phone': " + decodedToken.getClaim("phone"));
        System.out.println("claim 'roles': " + decodedToken.getClaim("roles"));


        System.out.println("end time: " + System.currentTimeMillis());
    }


    @Test
    void dbTest(){

        long startTime = System.currentTimeMillis();
        System.out.println("start time: " + startTime);

        User user = userMapper.getUserById(1);

        long endTime = System.currentTimeMillis();
        System.out.println("end time: " + startTime);

        System.out.println("elapsed: " + (endTime - startTime));

    }
}
