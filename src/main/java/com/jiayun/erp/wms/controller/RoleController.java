package com.jiayun.erp.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiayun.erp.wms.entity.Role;
import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.mapper.RoleMapper;
import com.jiayun.erp.wms.util.Res;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Optional;

@Api(value = "角色管理控制器")
@RestController
public class RoleController {

    @Autowired
    private RoleMapper roleMapper;

    @Order(1)
    @ApiOperation(value = "获取角色列表(分页)")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "页码", required = true),
                        @ApiImplicitParam(name = "pageSize", value = "每页数据条数", required = true)})
    @GetMapping("/roles")
    public ResponseEntity<Res> getRoleList(@RequestParam(required = false) Optional<Integer> pageNo, @RequestParam(required = false) Optional<Integer> pageSize) {

        int pageNum = pageNo.filter(n -> n > 0).orElse(1);
        int pageQuantity = pageSize.filter(n -> n > 0 && n < 50).orElse(20);
        Page<Role> page = new Page<>(pageNum, pageQuantity);

        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        queryWrapper.isNull("delete_date");

        return Res.ok("success", roleMapper.selectPage(page, queryWrapper));
    }

    @Order(2)
    @ApiOperation(value = "获取所有角色列表")
    @GetMapping("/role/get-all")
    public ResponseEntity<Res> getAllRoleList() {
        return Res.ok("success", roleMapper.getAllRoleList());
    }

    @Order(3)
    @ApiOperation(value = "获取角色详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleId", value = "角色ID", required = true)})
    @GetMapping("/role/{roleId}")
    public Role getRoleById(@PathVariable int roleId){
        return roleMapper.getRoleById(roleId);
    }

    @Order(4)
    @ApiOperation(value = "获取角色详情(带权限信息)")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleId", value = "角色ID", required = true)})
    @GetMapping("/role/with-permissions/{roleId}")
    public ResponseEntity<Res> getRoleWithPermissions(@PathVariable int roleId){
        return Res.ok("success", roleMapper.getRoleWithPermissions(roleId));
    }

    @Order(5)
    @ApiOperation(value = "新增角色")
    @ApiImplicitParams({@ApiImplicitParam(name = "key",value = "角色键值",required = true),
                        @ApiImplicitParam(name = "name",value = "角色名称",required = true),
                        @ApiImplicitParam(name = "description",value = "角色描述")})
    @PostMapping("/role")
    public ResponseEntity<Res> createRole(@ApiIgnore Role role){
        int created = roleMapper.createRole(role);
        if(created > 0){
            System.out.println(role);
        }
        return Res.ok("添加角色成功", null);
    }

    @Order(6)
    @ApiOperation(value = "新增角色(json)")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleKey",value = "角色键值",required = true),
                        @ApiImplicitParam(name = "name",value = "角色名称",required = true),
                        @ApiImplicitParam(name = "description",value = "角色描述"),
                        @ApiImplicitParam(name = "permissionIds",value = "权限ID")})
    @PostMapping("/role/createByJson")
    public ResponseEntity<Res> createRoleByJson(@ApiIgnore @RequestBody Role role){
        //TODO 校验角色是否为已删除角色，细化新增角色逻辑

        //TODO 添加数据库事务
        int created = roleMapper.insert(role);

        //创建角色-权限关联关系
        if(created == 1){
            int[] permissionIds = role.getPermissionIds();
            for (int permissionId : permissionIds){
                int res = roleMapper.createRolePermissions(role.getId(), permissionId);
            }
        }

        return Res.ok("添加角色成功", null);
    }

    @Order(7)
    @ApiOperation(value = "修改角色")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleKey",value = "角色键值",required = true),
                        @ApiImplicitParam(name = "name",value = "角色名称",required = true),
                        @ApiImplicitParam(name = "description",value = "角色描述"),
                        @ApiImplicitParam(name = "permissionIds",value = "权限ID")})
    @PutMapping("/role")
    public ResponseEntity<Res> updateRoleByJson(@ApiIgnore @RequestBody Role role){
        //TODO 添加数据库事务
        int updated = roleMapper.updateRole(role);

        //创建关联关系
        if(updated == 1){
            //删除原角色-权限关系
            int deleteCnt = roleMapper.deleteRolePermissionsByRoleId(role.getId());
            //创建新角色-权限关系
            int[] permissionIds = role.getPermissionIds();
            for (int permissionId : permissionIds){
                int res = roleMapper.createRolePermissions(role.getId(), permissionId);
            }
        }
        return Res.ok("修改角色成功", null);
    }

    @Order(8)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{roleId}")
    public ResponseEntity<Res> deleteRoleById(@PathVariable int roleId){
        System.out.println("delete role with id " + roleId);
        roleMapper.deleteRole(roleId);
        //删除角色-权限关联关系
        int deleteCnt = roleMapper.deleteRolePermissionsByRoleId(roleId);
        return Res.ok("删除角色成功", null);
    }


}
