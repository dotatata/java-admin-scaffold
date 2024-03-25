package com.jiayun.erp.wms.controller.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

//关闭Advice 使用时打开注释
//@ControllerAdvice
public class GlobalRequestParamHandler {

    @ModelAttribute
    public void handleRequestParam(@RequestParam("paramName") String paramValue, Model model) {
        // 全局请求参数处理逻辑
        model.addAttribute("paramName", paramValue);
    }

}
