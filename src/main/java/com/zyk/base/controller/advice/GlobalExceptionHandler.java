package com.zyk.base.controller.advice;

import com.zyk.base.util.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Res> handleException(Exception ex){
        log.error("请求失败: {}", ex.getMessage());
        return Res.failure("请求失败", ex.getMessage());
    }

}
