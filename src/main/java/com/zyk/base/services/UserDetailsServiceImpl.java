package com.zyk.base.services;

import com.zyk.base.entity.AuthUser;
import com.zyk.base.entity.Permission;
import com.zyk.base.entity.Role;
import com.zyk.base.entity.User;
import com.zyk.base.mapper.AuthMapper;
import com.zyk.base.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthMapper authMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO 放入缓存提高性能
        User authUser = authMapper.getUserAuthByPhone(username);
        if (authUser == null) {
            throw new UsernameNotFoundException("未找到此用户");
        }
        // 处理用户权限
        List<Permission> permissions;
        if(authUser.checkSuperAdmin()){
            // 超级管理员拥有所有权限
            permissions = permissionMapper.getAllPermissionList();
        }else if(!CollectionUtils.isEmpty(authUser.getRoles())) {
            int[] roleIds = authUser.getRoles().stream().mapToInt(Role::getId).toArray();
            permissions = permissionMapper.getPermissionsByRoleIds(roleIds);
        }else{
            permissions = new ArrayList<>();
        }
        List<GrantedAuthority> permissionKeys = permissions.stream().map(
                permission -> new SimpleGrantedAuthority(permission.getPermissionKey())
        ).collect(Collectors.toList());

        return new AuthUser(authUser, authUser.getPhone(), authUser.getPwd(), permissionKeys);
    }
}
