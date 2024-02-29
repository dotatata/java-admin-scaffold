package com.jiayun.erp.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiayun.erp.wms.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("select * from users")
    ArrayList<User> getAllUserList();

    @Select("select * from users where id = #{userId}")
    User getUserById(@Param("userId") int userId);

    User getUserWithRoles(@Param("userId") int userId);

    User getUserWithRolesByPhone(@Param("phone") String phone);

    @Insert("insert into users values (#{id}, #{name}, #{title}, #{phone}, #{salt}, #{pwd}, #{createDate}, #{updateDate}, #{deleteDate})")
    int createUser(User user);

    @Insert("insert into user_roles (user_id, role_id) values (#{userId}, #{roleId})")
    int createUserRoles(@Param("userId") int userId, @Param("roleId") int roleId);

    @Update("update users set name = #{name}, title = #{title}, phone = #{phone} where id = #{id}")
    int updateUser(User user);

    @Update("update users set delete_date = CURRENT_TIMESTAMP where id = #{userId}")
    int deleteUser(@Param("userId") int userId);

    @Delete("delete from user_roles where user_id = #{userId}")
    int deleteUserRolesByUserId(@Param("userId") int userId);
}
