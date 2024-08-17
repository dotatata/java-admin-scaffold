package com.zyk.base.controller.advice;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

//关闭Advice 使用时打开注释
//@ControllerAdvice
public class GlobalBindingInitializer {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 全局数据绑定初始化逻辑
        binder.setDisallowedFields("id");
    }
}
