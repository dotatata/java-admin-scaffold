package com.jiayun.erp.wms.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {

    @Insert("insert into test (role_id, user_id) values (#{roleId}, #{userId})")
    int createUserRoles(@Param("roleId") int roleId, @Param("userId") int userId);
}
