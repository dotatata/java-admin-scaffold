package com.jiayun.erp.wms;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.mapper.UserMapper;
import com.jiayun.erp.wms.util.JwtUtil;
import com.jiayun.erp.wms.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class WmsApplicationTests {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserMapper userMapper;


    @Test
    void contextLoads() {
    }

    @Test
    void redisTest(){
        System.out.println("start time: " + System.currentTimeMillis());
        String key = "user:auth";
        redisUtil.set(key, 1);

        int value = (int) redisUtil.get(key);
        System.out.println("key '" + key + "' value is " + value);
        System.out.println("end time: " + System.currentTimeMillis());

    }

    @Test
    void jwtTest(){
        System.out.println("start time: " + System.currentTimeMillis());

        User user = userMapper.getUserWithRolesByPhone("13888888888");


        Map<String, String> map = new HashMap<>();
        map.put("uid", user.getId().toString());
        map.put("phone", user.getPhone());
        map.put("name", user.getName());
        map.put("roles", user.getRoles().toString());
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

}
