package com.zyk.base.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zyk.base.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CurrentThread {

    public static final String USER_PROFILE_KEY_PREFIX = "user:";

    private final RedisUtil redisUtil;
    private final HttpServletRequest request;
    private final JsonDeserializer<Timestamp> customTimestampDeserializer;

    /**
     * 使用redis等缓存作为session管理时 获取用户
     * @return User
     */
    public User currentUserByRedis(){
        String token = request.getHeader("x-token");

        // 解析JWT token
        DecodedJWT decodedToken = JwtUtil.getDecodedToken(token);
        Integer userId = decodedToken.getClaim("uid").asInt();

        // 反序列化缓存的User
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Timestamp.class, customTimestampDeserializer);
        objectMapper.registerModule(module);

        return objectMapper.convertValue(redisUtil.get(authUserCacheKey(userId)), User.class);
    }

    /**
     * 此方法用于获取 JWT token中的user信息
     * @return User
     */
    public Map<String, Object> currentUserByJWT(){
        String token = request.getHeader("x-token");

        // 解析JWT token
        DecodedJWT decodedToken = JwtUtil.getDecodedToken(token);
        Integer userId = decodedToken.getClaim("uid").asInt();
        String phone = decodedToken.getClaim("phone").asString();
        String name = decodedToken.getClaim("name").asString();
        String[] roleKeys = decodedToken.getClaim("roles").asArray(String.class);

        //System.out.println("userId: " + userId);
        //System.out.println("phone: " + phone);
        //System.out.println("name: " + name);
        //System.out.println("roles: " + Arrays.toString(roleKeys));

        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("userId", userId);
        userProfileMap.put("phone", phone);
        userProfileMap.put("name", name);
        userProfileMap.put("roleKeys", roleKeys);

        return userProfileMap;
    }

    /**
     * 此方法用于获取 使用SpringSecurity + 纯JWT时 在过滤器(本项目命名为AuthenticationTokenFilter)中置入user对象
     * @return Object
     */
    public static User currentUserBySecurityFilterSet(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return (User) principal;
            }else {
                String username = principal.toString();
            }
        }else {
            return null;
        }
        return null;
    }

    public static String authUserCacheKey(int userId){
        return USER_PROFILE_KEY_PREFIX + userId;
    }

}
