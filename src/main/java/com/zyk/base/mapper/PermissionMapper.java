package com.zyk.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyk.base.entity.Permission;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select id, `permission_key`, `name`, description from permissions where delete_date is null")
    ArrayList<Permission> getAllPermissionList();

    ArrayList<Permission> getPermissionsByRoleIds(int[] roleIds);

    @Select("select * from permissions where id = #{permissionId}")
    Permission getPermissionById(@Param("permissionId") int permissionId);

    @Insert("insert into permissions values (#{id}, #{name}, #{permissionKey}, #{description}, #{createDate}, #{updateDate}, #{deleteDate})")
    int createPermission(Permission permission);

    @Update("update permissions set name = #{name}, permission_key = #{permissionKey}, description = #{description} where id = #{id}")
    int updatePermission(Permission permission);

    @Update("update permissions set delete_date = CURRENT_TIMESTAMP where id = #{permissionId}")
    int deletePermission(@Param("permissionId") int permissionId);

}
