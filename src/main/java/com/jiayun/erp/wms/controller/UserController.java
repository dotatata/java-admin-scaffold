package com.jiayun.erp.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.mapper.UserMapper;
import com.jiayun.erp.wms.util.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

@Api(value = "用户控制器")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Order(1)
    @ApiOperation(value = "获取用户列表(分页)")
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "用户名称", required = false),
                        @ApiImplicitParam(name = "title", value = "用户岗位", required = false),
                        @ApiImplicitParam(name = "phone", value = "用户电话", required = false),
                        @ApiImplicitParam(name = "page", value = "页码", required = false),
                        @ApiImplicitParam(name = "limit", value = "每页数据条数", required = false),
                        @ApiImplicitParam(name = "sort", value = "排序字段", required = false),
                        @ApiImplicitParam(name = "rank", value = "排序顺序规则ASC|DESC", required = false, allowableValues="ASC|DESC")})
    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/users")
    public ResponseEntity<Res> getUserList(@RequestParam(required = false) Optional<String> name,
                                           @RequestParam(required = false) Optional<String> title,
                                           @RequestParam(required = false) Optional<String> phone,
                                           @RequestParam(required = false) Optional<Integer> page,
                                           @RequestParam(required = false) Optional<Integer> limit,
                                           @RequestParam(required = false) Optional<String> sort,
                                           @RequestParam(required = false) Optional<String> rank) {

        int pageNum = page.filter(n -> n > 0).orElse(1);
        int pageQuantity = limit.filter(n -> n > 0 && n < 50).orElse(20);
        Page<User> pagination = new Page<>(pageNum, pageQuantity);

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.isNull("delete_date");
        //模糊搜索用户名称
        name.ifPresent(value -> queryWrapper.like("name", value));
        //模糊搜索用户岗位
        title.ifPresent(value -> queryWrapper.like("title", value));
        //模糊搜索用户电话
        phone.ifPresent(value -> queryWrapper.like("phone", value));

        //排序逻辑 sort + rank
        boolean isASC = rank.orElse("ASC").equals("ASC");
        queryWrapper.orderBy(true, isASC, sort.orElse("id"));

        return Res.ok("success", userMapper.selectPage(pagination, queryWrapper));
    }

    @Order(2)
    @ApiOperation(value = "获取所有用户列表")
    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/user/get-all")
    public ArrayList<User> getAllUserList() {
        return userMapper.getAllUserList();
    }

    @Order(3)
    @ApiOperation(value = "获取用户详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户ID", required = true)})
    @PreAuthorize("hasAnyAuthority('user:read')")
    @GetMapping("/user/{userId}")
    public User getUserById(@PathVariable int userId){
        System.out.println("get user by id " + userId);
        return userMapper.getUserById(userId);
    }

    @Order(4)
    @ApiOperation(value = "获取用户详情(带角色信息)")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户ID", required = true)})
    @GetMapping("/user/with-roles/{userId}")
    public ResponseEntity<Res> getUserWithRoles(@PathVariable int userId){
        return Res.ok("success", userMapper.getUserWithRoles(userId));
    }

    @Order(5)
    @ApiOperation(value = "新增用户")
    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "姓名",required = true),
                        @ApiImplicitParam(name = "title",value = "岗位"),
                        @ApiImplicitParam(name = "phone",value = "电话",required = true),
                        @ApiImplicitParam(name = "pwd",value = "密码",required = true)})
    @PreAuthorize("hasAnyAuthority('user:create')")
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, timeout = 3, rollbackFor = {Exception.class})
    @PostMapping("/user")
    public ResponseEntity<Res> createUser(@ApiIgnore User user){
        System.out.println(user);
        //TODO 添加事务
        int created = userMapper.createUser(user);
        if(created > 0){
            System.out.println(user);
        }
        return Res.success(HttpStatus.CREATED, "添加用户成功", null);
    }

    @Order(6)
    @ApiOperation(value = "新增用户(json)")
    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "姓名",required = true),
                        @ApiImplicitParam(name = "title",value = "岗位"),
                        @ApiImplicitParam(name = "phone",value = "电话",required = true),
                        @ApiImplicitParam(name = "pwd",value = "密码"),
                        @ApiImplicitParam(name = "roleIds",value = "角色ID")})
    @PreAuthorize("hasAnyAuthority('user:create')")
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, timeout = 3, rollbackFor = {Exception.class})
    @PostMapping("/user/createByJson")
    public ResponseEntity<Res> createUserByJson(@RequestBody User user){
        //TODO 校验电话是否为已删除用户，如果为已删除用户，则更新用户信息，并重新启用

        if (user.getPwd() == null) {
            user.setPwd("ooxxooxx");
        }
        user.setPwd(passwordEncoder.encode(user.getPwd()));

        //TODO 添加数据库事务
        int created = userMapper.insert(user);

        //创建关联关系
        if(created == 1){
            int[] roleIds = user.getRoleIds();
            //TODO userMapper中增加一次insert多条数据的方法 避免循环
            for (int roleId : roleIds){
                int res = userMapper.createUserRoles(user.getId(), roleId);
            }
        }

        return Res.ok("添加用户成功", null);
    }

    @Order(7)
    @ApiOperation(value = "修改用户")
    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "姓名",required = true),
                        @ApiImplicitParam(name = "title",value = "岗位"),
                        @ApiImplicitParam(name = "phone",value = "电话",required = true),
                        @ApiImplicitParam(name = "pwd",value = "密码")})
    @PreAuthorize("hasAnyAuthority('user:update')")
    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3, rollbackFor = {Exception.class})
    @PutMapping("/user")
    public ResponseEntity<Res> updateUserByJson(@ApiIgnore @RequestBody User user){
        System.out.println("update user " + user);
        //TODO 添加数据库事务
        int updated = userMapper.updateUser(user);

        //创建关联关系
        if(updated == 1){
            //删除原角色关系
            int deleteCnt = userMapper.deleteUserRolesByUserId(user.getId());
            //创建新角色关系
            int[] roleIds = user.getRoleIds();
            //TODO userMapper中增加一次insert多条数据的方法 避免循环
            for (int roleId : roleIds){
                int res = userMapper.createUserRoles(user.getId(), roleId);
            }
        }
        return Res.ok("修改用户成功", null);
    }

    @Order(8)
    @ApiOperation(value = "删除用户")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3, rollbackFor = {Exception.class})
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Res> deleteUserById(@PathVariable int userId){
        System.out.println("delete user with id " + userId);
        userMapper.deleteUser(userId);
        //删除用户-角色关联关系
        int deleteCnt = userMapper.deleteUserRolesByUserId(userId);
        return Res.ok("删除用户成功", null);
    }
}
