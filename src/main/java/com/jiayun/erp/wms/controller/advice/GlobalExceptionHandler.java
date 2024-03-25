package com.jiayun.erp.wms.controller.advice;

import com.jiayun.erp.wms.util.Res;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Res> handleException(Exception ex){
        return Res.failure("请求失败", ex.getMessage());
    }

}
