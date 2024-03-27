package com.jiayun.erp.wms.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jiayun.erp.wms.entity.DataPermissionTest;
import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.mapper.DataPermissionTestMapper;
import com.jiayun.erp.wms.mapper.TestMapper;
import com.jiayun.erp.wms.util.CurrentThread;
import com.jiayun.erp.wms.util.JwtUtil;
import com.jiayun.erp.wms.util.RedisUtil;
import com.jiayun.erp.wms.util.Res;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestMapper testMapper;
    private final DataPermissionTestMapper dataPermissionTestMapper;
    private final CurrentThread currentThread;

    private final SqlSessionFactory sqlSessionFactory;
    private final PlatformTransactionManager platformTransactionManager;

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
    public ResponseEntity<Res> testTokenAction(HttpServletRequest request){

        String token = request.getHeader("x-token");
        System.out.println("X-Token: " + token);

        System.out.println("get AuthUser from redis: " + currentThread.currentUserByRedis());
        return Res.ok("success", null);
    }

    @GetMapping("/global-exception-test")
    public ResponseEntity<Res> testGlobalException() throws Exception {
        throw new Exception("全局异常响应测试");
    }

    @GetMapping("/transaction-test")
    public ResponseEntity<Res> testManualTransaction(@RequestParam int userId, @RequestParam int roleId) {

        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus txStatus = platformTransactionManager.getTransaction(txDef);
        try {
            //if(testMapper.createUserRoles(roleId, userId) == 1){
            //    throw new Exception("手动抛错,制造回滚");
            //}
            testMapper.createUserRoles(roleId, userId);
            platformTransactionManager.commit(txStatus);
        }catch (Exception e){
            platformTransactionManager.rollback(txStatus);
            return Res.ok(e.getMessage());
        }

        return Res.ok("success");
    }

}
