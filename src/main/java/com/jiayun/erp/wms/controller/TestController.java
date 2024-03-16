package com.jiayun.erp.wms.controller;

import com.jiayun.erp.wms.entity.DataPermissionTest;
import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.mapper.DataPermissionTestMapper;
import com.jiayun.erp.wms.mapper.TestMapper;
import com.jiayun.erp.wms.util.CurrentThread;
import com.jiayun.erp.wms.util.RedisUtil;
import com.jiayun.erp.wms.util.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    TestMapper testMapper;
    @Autowired
    DataPermissionTestMapper dataPermissionTestMapper;
    @Autowired
    CurrentThread currentThread;

    @GetMapping("/test")
    public ResponseEntity<String> testAction(){
        return new ResponseEntity<String>("test controller", HttpStatus.OK);
    }

    @GetMapping("/data-permission")
    public ResponseEntity<Res> testDataPermission() {
        List<DataPermissionTest> dataPermissionTests = dataPermissionTestMapper.getAllList("desc test");
        return Res.ok("success", dataPermissionTests);
    }

    @GetMapping("/token-test")
    public ResponseEntity<Res> testTokenAction(){
        System.out.println("get AuthUser from redis: " + currentThread.currentUserByRedis());
        return Res.ok("success", null);
    }
}
