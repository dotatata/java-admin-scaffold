package com.jiayun.erp.wms.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JwtUtil {

    public static String secret = "abcdefghijklmnopqrstuvwxyz";

    public static long time = 7 * 24 * 60 * 60 * 1000L;


    public static String createTokenByMap(Map<String, Object> map) {

        JWTCreator.Builder builder = JWT.create();

        map.forEach((key, value) -> {
            if(value instanceof Boolean){
                builder.withClaim(key, (Boolean) value);
            } else if(value instanceof Integer){
                builder.withClaim(key, (Integer) value);
            } else if(value instanceof Long){
                builder.withClaim(key, (Long) value);
            } else if(value instanceof Double){
                builder.withClaim(key, (Double) value);
            } else if(value instanceof String){
                builder.withClaim(key, (String) value);
            } else if(value instanceof Date){
                builder.withClaim(key, (Date) value);
            } else if(value instanceof Map){
                builder.withClaim(key, (Map<String, ?>) value);
            } else if(value instanceof List){
                builder.withClaim(key, (List<?>) value);
            } else if(value instanceof Integer[]){
                builder.withArrayClaim(key, (Integer[]) value);
            } else if(value instanceof Long[]){
                builder.withArrayClaim(key, (Long[]) value);
            } else if(value instanceof String[]){
                builder.withArrayClaim(key, (String[]) value);
            }
        });

        builder.withExpiresAt(new Date(System.currentTimeMillis() + time));

        return builder.sign(Algorithm.HMAC256(secret));
    }

    public static String createToken(String key, String value){
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim(key, value);
        builder.withExpiresAt(new Date(System.currentTimeMillis() + time));

        return builder.sign(Algorithm.HMAC256(secret));
    }

    public static String getClaim(String token, String key){
        // 验证签名
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT decodedToken = verifier.verify(token);
        return decodedToken.getClaim(key).asString();
    }

    public static DecodedJWT getDecodedToken(String token){
        // 验证签名
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(token);
    }

    //public static Map<String, String> getTokenMap(String token, String key){
    //    // 验证签名
    //    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
    //    DecodedJWT decodedToken = verifier.verify(token);
    //    return decodedToken.getPayload();
    //}

    // 加密有效 & 未超时
    public static boolean isValidate(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        try{
            DecodedJWT decodedToken = verifier.verify(token);
            return true;
        }catch (Exception e){
            //TODO 打印错误日志
            e.printStackTrace();
        }
        return false;
    }





}
