package com.zyk.base.controller.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

//关闭Advice 使用时打开注释
//@ControllerAdvice
public class GlobalInterceptor {

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        // 全局请求拦截逻辑
        model.addAttribute("attributeName", "attributeValue");
    }

}
