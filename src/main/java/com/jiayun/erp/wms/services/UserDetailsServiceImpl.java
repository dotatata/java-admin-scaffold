package com.jiayun.erp.wms.services;

import com.jiayun.erp.wms.entity.Permission;
import com.jiayun.erp.wms.entity.Role;
import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.mapper.PermissionMapper;
import com.jiayun.erp.wms.mapper.RoleMapper;
import com.jiayun.erp.wms.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    PermissionMapper permissionMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.getUserAuthByPhone(username);
        if (user == null) {
            throw new UsernameNotFoundException("未找到此用户");
        }
        // 处理用户权限
        List<Permission> permissions;
        if(user.checkSuperAdmin()){
            // 超级管理员拥有所有权限
            permissions = permissionMapper.getAllPermissionList();
        }else if(!CollectionUtils.isEmpty(user.getRoles())) {
            int[] roleIds = user.getRoles().stream().mapToInt(Role::getId).toArray();
            permissions = permissionMapper.getPermissionsByRoleIds(roleIds);
        }else{
            permissions = new ArrayList<>();
        }
        List<GrantedAuthority> permissionKeys = permissions.stream().map(
                permission -> new SimpleGrantedAuthority(permission.getPermissionKey())
        ).collect(Collectors.toList());

        //直接使用SpringSecurity的user对象 不想再封装为业务user了
        return new org.springframework.security.core.userdetails.User(user.getPhone(), user.getPwd(), permissionKeys);
    }
}
