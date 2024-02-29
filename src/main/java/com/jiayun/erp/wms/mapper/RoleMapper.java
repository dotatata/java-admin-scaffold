package com.jiayun.erp.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiayun.erp.wms.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id, `role_key`, `name`, description from roles where `role_key` != 'super-admin' and delete_date is null")
    ArrayList<Role> getAllRoleList();

    @Select("select * from roles where id = #{roleId}")
    Role getRoleById(int roleId);

    Role getRoleWithPermissions(@Param("roleId") int roleId);

    @Insert("insert into roles values (#{id}, #{name}, #{description}, #{createDate}, #{updateDate}, #{deleteDate})")
    int createRole(Role role);

    @Insert("insert into role_permissions (role_id, permission_id) values (#{roleId}, #{permissionId})")
    int createRolePermissions(@Param("roleId") int roleId, @Param("permissionId") int permissionId);

    @Update("update roles set name = #{name}, role_key = #{roleKey}, description = #{description} where id = #{id}")
    int updateRole(Role role);

    @Update("update roles set delete_date = CURRENT_TIMESTAMP where id = #{roleId}")
    int deleteRole(@Param("roleId") int roleId);

    @Delete("delete from role_permissions where role_id = #{roleId}")
    int deleteRolePermissionsByRoleId(@Param("roleId") int roleId);
}
