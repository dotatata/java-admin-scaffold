package com.jiayun.erp.wms.mapper;

import com.jiayun.erp.wms.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {

    @Select("select * from users where phone = #{phone}")
    User getAuthUserByPhone(@Param("phone") String phone);

    //用户认证鉴权时调用(含登录密码、角色列表信息)
    User getUserAuthByPhone(@Param("phone") String phone);
}
