package com.zyk.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyk.base.entity.Permission;
import com.zyk.base.mapper.PermissionMapper;
import com.zyk.base.util.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@Api(value = "权限管理控制器")
@RestController
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionMapper permissionMapper;

    @Order(1)
    @ApiOperation(value = "获取权限列表(分页)")
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "权限名称"),
                        @ApiImplicitParam(name = "permissionKey", value = "权限键值"),
                        @ApiImplicitParam(name = "pageNo", value = "页码", required = true),
                        @ApiImplicitParam(name = "pageSize", value = "每页数据条数", required = true)})
    @GetMapping("/permissions")
    public ResponseEntity<Res> getPermissionList(@RequestParam(required = false) Optional<String> name,
                                                 @RequestParam(required = false) Optional<String> permissionKey,
                                                 @RequestParam(required = false) Optional<Integer> pageNo,
                                                 @RequestParam(required = false) Optional<Integer> pageSize,
                                                 @RequestParam(required = false) Optional<String> sort,
                                                 @RequestParam(required = false) Optional<String> rank) {

        int pageNum = pageNo.filter(n -> n > 0).orElse(1);
        int pageQuantity = pageSize.filter(n -> n > 0 && n < 50).orElse(20);
        Page<Permission> page = new Page<>(pageNum, pageQuantity);

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
        queryWrapper.isNull("delete_date");
        //模糊搜索权限名称
        name.ifPresent(value -> queryWrapper.like("name", value));
        //模糊搜索权限键值
        permissionKey.ifPresent(value -> queryWrapper.like("permission_key", value));

        //排序逻辑 sort + rank
        boolean isASC = rank.orElse("ASC").equals("ASC");
        queryWrapper.orderBy(true, isASC, sort.orElse("id"));

        return Res.ok("success", permissionMapper.selectPage(page, queryWrapper));
    }

    @Order(2)
    @ApiOperation(value = "获取所有权限列表")
    @GetMapping("/permission/get-all")
    public ResponseEntity<Res> getAllPermissionList() {
        return Res.ok("success", permissionMapper.getAllPermissionList());
    }

    @Order(3)
    @ApiOperation(value = "获取权限详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "permissionId", value = "权限ID", required = true)})
    @GetMapping("/permission/{permissionId}")
    public ResponseEntity<Res> getPermissionById(@PathVariable int permissionId){
        return Res.ok("success", permissionMapper.getPermissionById(permissionId));
    }

    @Order(4)
    @ApiOperation(value = "新增权限(json)")
    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "权限名称",required = true),
                        @ApiImplicitParam(name = "permissionKey",value = "权限键值",required = true),
                        @ApiImplicitParam(name = "description",value = "权限描述",required = true)})
    @PostMapping("/permission")
    public ResponseEntity<Res> createPermissionByJson(@RequestBody Permission permission){
        //TODO 添加事务
        int created = permissionMapper.createPermission(permission);

        return Res.success(HttpStatus.CREATED, "添加权限成功", null);
    }

    @Order(5)
    @ApiOperation(value = "修改权限")
    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "权限名称",required = true),
                        @ApiImplicitParam(name = "permissionKey",value = "权限键值",required = true),
                        @ApiImplicitParam(name = "description",value = "权限描述",required = true)})
    @PutMapping("/permission")
    public ResponseEntity<Res> updatePermissionByJson(@ApiIgnore @RequestBody Permission permission){
        //TODO 添加数据库事务
        int updated = permissionMapper.updatePermission(permission);

        return Res.ok("修改权限成功", null);
    }

    @Order(6)
    @ApiOperation(value = "删除权限")
    @DeleteMapping("/permission/{permissionId}")
    public ResponseEntity<Res> deletePermissionById(@PathVariable int permissionId){
        permissionMapper.deletePermission(permissionId);
        return Res.ok("删除权限成功", null);
    }
}
