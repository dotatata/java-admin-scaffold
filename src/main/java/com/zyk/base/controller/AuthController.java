package com.zyk.base.controller;

import com.zyk.base.entity.AuthUser;
import com.zyk.base.entity.Permission;
import com.zyk.base.entity.Role;
import com.zyk.base.entity.User;
import com.zyk.base.mapper.PermissionMapper;
import com.zyk.base.mapper.UserMapper;
import com.zyk.base.util.CurrentThread;
import com.zyk.base.util.JwtUtil;
import com.zyk.base.util.RedisUtil;
import com.zyk.base.util.Res;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Authentication & Authorization **/
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final AuthenticationManager authenticationManager;
    private final RedisUtil redisUtil;
    private final CurrentThread currentThread;

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
                // 认证成功 处理需要放到header中的认证token信息
                AuthUser authUser = (AuthUser)authentication.getPrincipal();

                //构造token中的信息
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("uid", authUser.getUser().getId());
                userMap.put("phone", authUser.getUser().getPhone());
                userMap.put("name", authUser.getUser().getName());
                userMap.put("roles", authUser.getUser().getRoles().stream().map(Role::getRoleKey).toArray(String[]::new));

                String token = JwtUtil.createTokenByMap(userMap);

                // 返回认证token 后续访问从header中的"X-token"中获取认证
                Map<String, String> data = new HashMap<>();
                data.put("token", token);

                // 设置Redis缓存
                redisUtil.set(CurrentThread.authUserCacheKey(authUser.getUser().getId()), authUser.getUser());

                return Res.ok("登录成功~~", data);
            }
        }catch (BadCredentialsException e){
            return Res.failure("登录失败:账号或密码错误!!");
        }catch (Exception e){
            return Res.failure(e.getMessage());
        }

        return Res.error(HttpStatus.INTERNAL_SERVER_ERROR, "登录服务发生错误", null);
    }

    @Order(2)
    @ApiOperation(value = "获取当前登录用户详情(带角色信息)")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "vueElementAdminToken", required = true)})
    @GetMapping("/auth/user-info-by-token")
    public ResponseEntity<Res> getUserInfoByToken(@RequestParam String token){

        String phone = currentThread.currentUserByJWT().get("phone").toString();

        User user = userMapper.getUserWithRolesByPhone(phone);

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


        //TODO 清除Session: JWT失效、redis删除

        // 删除redis键值
        int userId = (int) currentThread.currentUserByJWT().get("userId");
        redisUtil.del(CurrentThread.authUserCacheKey(userId));

        return Res.ok("success", null);
    }
}
