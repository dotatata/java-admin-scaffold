package com.jiayun.erp.wms.mapper;

import com.jiayun.erp.wms.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {

    @Select("select * from users where phone = #{phone}")
    User getUserByPhone(@Param("phone") String phone);
}
