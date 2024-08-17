package com.zyk.base.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {

    @Insert("insert into test (role_id, user_id) values (#{roleId}, #{userId})")
    int createUserRoles(@Param("roleId") int roleId, @Param("userId") int userId);

    @Insert("insert into test (role_id, user_id) values (#{roleId}, 2)")
    int createUser2Roles(@Param("roleId") int roleId);
}
