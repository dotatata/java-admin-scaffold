package com.zyk.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class Res {

    //TODO code枚举改造
    private int code;
    private String message;
    private Object data;

    private Res(){}

    public Res(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // HTTP code 自定义 && 服务正常; Restful eg: 实体创建成功201
    public static ResponseEntity<Res> success(HttpStatus httpStatus, String message, Object data) throws IllegalArgumentException {
        if(httpStatus.is2xxSuccessful()){
            //TODO code枚举改造
            Res res = new Res(20000, message, data);
            return ResponseEntity.status(httpStatus).body(res);
        }else {
            throw new IllegalArgumentException("HttpStatus is NOT in the HTTP SUCCESSFUL series");
        }
    }

    // HTTP code 自定义 && 服务错误
    public static ResponseEntity<Res> error(HttpStatus httpStatus, String message, Object data) throws IllegalArgumentException {
        if(httpStatus.is5xxServerError() || httpStatus.is4xxClientError()){
            //TODO code枚举改造
            Res res = new Res(50000, message, data);
            return ResponseEntity.status(httpStatus).body(res);
        }else {
            throw new IllegalArgumentException("HttpStatus is NOT in the HTTP SERVER_ERROR|CLIENT_ERROR series");
        }
    }

    // HTTP code 200 && 服务正常
    public static ResponseEntity<Res> ok(String message, Object data){
        //TODO code枚举改造
        Res res = new Res(20000, message, data);
        return ResponseEntity.ok(res);
    }

    // HTTP code 200 && 服务正常
    public static ResponseEntity<Res> ok(String message){
        return ok(message, null);
    }

    // HTTP code 200 && 服务失败
    public static ResponseEntity<Res> failure(String message, Object data){
        //TODO code枚举改造
        Res res = new Res(50000, message, data);
        return ResponseEntity.ok(res);
    }

    // HTTP code 200 && 服务失败
    public static ResponseEntity<Res> failure(String message){
        return failure(message, null);
    }



    // 容器管理之外使用 如filter等
    public static String write(int code, String message, Object data) throws JsonProcessingException {
        Res res = new Res(code, message, data);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(res);
    }


}
