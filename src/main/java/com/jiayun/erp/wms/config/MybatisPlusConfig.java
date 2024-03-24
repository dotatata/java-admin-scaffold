package com.jiayun.erp.wms.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.jiayun.erp.wms.util.CurrentThread;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@MapperScan("com.jiayun.erp.wms.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantLineHandler tenantLineHandler, DataPermissionHandler dataPermissionHandler) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //interceptor.addInnerInterceptor(new DataPermissionInterceptor(dataPermissionHandler));    //数据权限拦截器插件配置
        //interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(tenantLineHandler));       //多租户行权限拦截器插件配置
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));              //如果有多数据源可以不配具体类型 否则都建议配上具体的DbType
        //interceptor.addInnerInterceptor(new PaginationInnerInterceptor());                        //如果配置多个插件 切记分页最后添加
        return interceptor;
    }

    @Bean
    public TenantLineHandler tenantLineHandler(){
        return new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                //System.out.println("CurrentThread.currentUser: " + CurrentThread.currentUser().getUserId());
                System.out.println("CurrentThread.currentUser: " + CurrentThread.currentUserBySecurityFilterSet().getName());
                //TODO 先写死租户ID测试
                return new LongValue(2);
            }
            @Override
            public String getTenantIdColumn() {
                return "dept_id";
            }
            // true表示忽略 false 表示不忽略(添加数据行租户条件)
            @Override
            public boolean ignoreTable(String tableName) {
                List<String> tables = Arrays.asList(
                        "users", "user_roles", "roles", "role_permissions", "permissions"
                );
                return tables.contains(tableName.toLowerCase());
                //return !"data_permission_test".equalsIgnoreCase(tableName);
            }
        };
    }

    @Bean
    public DataPermissionHandler dataPermissionHandler(){
        return new DataPermissionHandler(){
            @SneakyThrows
            @Override
            public Expression getSqlSegment(Expression where, String mappedStatementId) {
                String sqlSegment = sqlSegmentMap.get(mappedStatementId);

                if(sqlSegment == null) return where;

                Expression sqlSegmentExpression = CCJSqlParserUtil.parseCondExpression(sqlSegment);
                if (where != null) {
                    return new AndExpression(where, sqlSegmentExpression);
                }else{
                    return sqlSegmentExpression;
                }
            }
        };
    }

    //TODO 根据当前用户及功能要求补充数据范围边界
    private static Map<String, String> sqlSegmentMap = new HashMap<String, String>() {
        {
            put("com.jiayun.erp.wms.mapper.DataPermissionTestMapper.getAllList", "user_id IN (1,2,3)");
            put("userIdGt", "user_id > 2");
            put("userIdGe", "user_id >= 2");
            put("contextBetween", "user_id >= 2 and userId <= 4");
            put("userIdLt", "user_id < 2");
            put("userIdLe", "user_id <= 2");
            put("contextLikeLeft", "context like 'te%'");
            put("contextLikeRight", "context like '%st'");
        }
    };
}
