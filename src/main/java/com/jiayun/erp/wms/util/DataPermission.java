package com.jiayun.erp.wms.util;


import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DataPermission {

    /**
     * 指明表权限字段
     * 可选值：user_id
     */
    String column();

    /**
     * TODO 修改为枚举
     * 可选值：= in >= <= like
     */
    String operator();

    /**
     * TODO 修改为枚举
     * 可选值：currentUser|currentDept
     */
    String value();
}
