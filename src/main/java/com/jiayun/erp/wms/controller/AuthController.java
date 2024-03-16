package com.jiayun.erp.wms.controller;

import com.jiayun.erp.wms.entity.Permission;
import com.jiayun.erp.wms.entity.Role;
import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.mapper.AuthMapper;
import com.jiayun.erp.wms.mapper.PermissionMapper;
import com.jiayun.erp.wms.mapper.UserMapper;
import com.jiayun.erp.wms.util.JwtUtil;
import com.jiayun.erp.wms.util.RedisUtil;
import com.jiayun.erp.wms.util.Res;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private PermissionMapper permissionMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisUtil redisUtil;

    @Order(1)
    @ApiOperation(value = "登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", required = true),
                        @ApiImplicitParam(name = "password", value = "密码", required = true)})
    @PostMapping("/auth/login")
    public ResponseEntity<Res> login(@NotNull @RequestBody Map<String, String> tokenPair) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(tokenPair.get("username"), tokenPair.get("password"));
        try{
            Authentication authentication = authenticationManager.authenticate(authToken);
            if (authentication != null && authentication.isAuthenticated()){
                Map<String, String> data = new HashMap<>();
                //User user = (User)authentication.getPrincipal();
                User user = authMapper.getUserByPhone(tokenPair.get("username"));

                Map<String, String> userMap = new HashMap<>();
                userMap.put("uid", user.getId().toString());
                userMap.put("phone", user.getPhone());
                userMap.put("name", user.getName());
                userMap.put("roles", user.getRoles().toString());

                String token = JwtUtil.createTokenByMap(userMap);
                // 返回认证token 后续访问从header中的"X-token"中获取认证
                data.put("token", token);

                //TODO 暂时使用userId替代vue-element-admin使用的token
                //data.put("token", user.getPhone());

                // 设置Redis缓存
                redisUtil.set("user:"+user.getId(), user);


                return Res.ok("登录成功~~", data);
            }
        }catch (BadCredentialsException e){
            return Res.error("登录失败:账号或密码错误!!");
        }catch (Exception e){
            return Res.error(e.getMessage());
        }

        return Res.failure(HttpStatus.INTERNAL_SERVER_ERROR, "登录服务发生错误", null);
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
        data.put("introduction", introduction);
        // 初始化认证用户的角色
        String[] roles = user.getRoles().stream().map(Role::getRoleKey).toArray(String[]::new);
        data.put("roles", roles);
        // 初始化认证用户的权限
        List<Permission> permissionList;
        if(user.checkSuperAdmin()){
            // 超级管理员拥有所有权限
            permissionList = permissionMapper.getAllPermissionList();
        }else if(!CollectionUtils.isEmpty(user.getRoles())) {
            int[] roleIds = user.getRoles().stream().mapToInt(Role::getId).toArray();
            permissionList = permissionMapper.getPermissionsByRoleIds(roleIds);
        }else{
            permissionList = new ArrayList<>();
        }
        String[] permissions = permissionList.stream().map(Permission::getPermissionKey).toArray(String[]::new);
        data.put("permissions", permissions);

        return Res.ok("success", data);
    }

    @Order(3)
    @ApiOperation(value = "登出")
    @PostMapping("/auth/logout")
    public ResponseEntity<Res> logout(String token) {
        System.out.println("account logout is: " + token);
        //TODO 清除Session
        return Res.ok("success", null);
    }
}
