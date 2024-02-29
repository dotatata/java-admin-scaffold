package com.jiayun.erp.wms.controller;

import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    TestMapper testMapper;

    @GetMapping("/test")
    public ResponseEntity<String> testAction(){
        return new ResponseEntity<String>("test controller", HttpStatus.OK);
    }
}
