package com.jiayun.erp.wms.controller;

import com.jiayun.erp.wms.entity.Role;
import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.mapper.AuthMapper;
import com.jiayun.erp.wms.mapper.UserMapper;
import com.jiayun.erp.wms.util.Res;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Authentication & Authorization **/
@RestController
public class AuthController {

    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Order(1)
    @ApiOperation(value = "登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", required = true),
                        @ApiImplicitParam(name = "password", value = "密码", required = true)})
    @PostMapping("/auth/login")
    public ResponseEntity<Res> login(@RequestBody Map<String, String> tokenPair) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(tokenPair.get("username"), tokenPair.get("password"));
        Authentication authentication = authenticationManager.authenticate(token);
        if (authentication != null && authentication.isAuthenticated()){
            Map<String, String> data = new HashMap<>();
            //User user = (User)authentication.getPrincipal();
            User user = authMapper.getUserByPhone(tokenPair.get("username"));
            //TODO 暂时使用userId替代vue-element-admin使用的token
            data.put("token", user.getPhone());
            return Res.ok("登录成功~~", data);
        }else {
            throw new BadCredentialsException("登录失败:账号或密码错误!!");
        }
    }

    @Order(2)
    @ApiOperation(value = "获取当前登录用户详情(带角色信息)")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "vueElementAdminToken", required = true)})
    @GetMapping("/auth/user-info-by-token")
    public ResponseEntity<Res> getUserInfoByToken(@RequestParam String token){

        User user = userMapper.getUserWithRolesByPhone(token);

        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getName());
        data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        String introduction = user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", "));
        System.out.println(introduction);
        data.put("introduction", introduction);
        String[] roles = user.getRoles().stream().map(Role::getRoleKey).toArray(String[]::new);
        data.put("roles", roles);

        System.out.println(data);

        return Res.ok("success", data);
    }

    @Order(3)
    @ApiOperation(value = "登出")
    @PostMapping("/auth/logout")
    public ResponseEntity<Res> logout(){
        //TODO 清除Session
        return Res.ok("success", null);
    }
}
