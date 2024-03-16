package com.jiayun.erp.wms.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiayun.erp.wms.entity.DataPermissionTest;
import com.jiayun.erp.wms.entity.User;
import com.jiayun.erp.wms.util.DataPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface DataPermissionTestMapper extends BaseMapper<DataPermissionTest> {

    @Select("select * from data_permission_test where `desc` = #{desc}")
    @DataPermission(column = "dept_id", operator = ">=", value = "4")
    //MybatisPlus拦截器插件配置 TenantLineInnerInterceptor DataPermissionInterceptor
    //@InterceptorIgnore(tenantLine = "false", dataPermission = "0")
    ArrayList<DataPermissionTest> getAllList(@Param("desc") String desc);

    @Select("select * from users where id = #{userId}")
    User getUserById(@Param("userId") int userId);
}
