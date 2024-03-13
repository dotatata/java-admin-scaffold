package com.jiayun.erp.wms.interceptors;

import com.jiayun.erp.wms.util.DataPermission;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Intercepts({//@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
             @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
             @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DataPermissionByAnnotationInterceptor implements Interceptor {

    private static final String COUNT_SUFFIX = "_COUNT";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 线程安全
        //AtomicReference<User> userAtomicReference = new AtomicReference<>();
        // 当前用户
        //userAtomicReference.set(CurrentThread.currentUser());

        // 本次调用的mapper对象及方法
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // 本次调用的mapper对象方法的参数
        //Object paramObj = invocation.getArgs()[1];
        //BoundSql boundSql = mappedStatement.getBoundSql(paramObj);


        String mappedStatementId = mappedStatement.getId();
        String mapperMethodName = mappedStatementId.substring(mappedStatementId.lastIndexOf('.')+1);
        String mapperClassName = mappedStatementId.substring(0, mappedStatementId.lastIndexOf('.'));

        // 分页插件会生成一个count语句，这个语句的参数也要做处理
        if (mapperMethodName.endsWith(COUNT_SUFFIX)) {
            mapperMethodName = mapperMethodName.substring(0, mapperMethodName.lastIndexOf(COUNT_SUFFIX));
        }
        // 获取mapper中声明的方法(不含父类的继承方法) 动态加载类并获取类中的方法
        final Method[] mapperMethods = Class.forName(mapperClassName).getDeclaredMethods();

        // 遍历类的所有方法 找到此次调用的方法
        for (Method mapperMethod : mapperMethods){
            if(mapperMethod.getName().equals(mapperMethodName) && mapperMethod.isAnnotationPresent(DataPermission.class)){
                //获取注解配置的参数 "user_id"
                String columnName = mapperMethod.getAnnotation(DataPermission.class).column();
                String operator = mapperMethod.getAnnotation(DataPermission.class).operator();
                String value = mapperMethod.getAnnotation(DataPermission.class).value();
                // 获取boundSql MybatisPlus的拦截器中 拦截Mybatis查询 并默认使用Executor中含缓存的query方法进行查询
                if(invocation.getArgs()[5] != null && invocation.getArgs()[5] instanceof BoundSql){
                    //获取已绑定的sql对象
                    BoundSql boundSql = (BoundSql)invocation.getArgs()[5];
                    //获取绑定sql的字符串
                    String originalSql = boundSql.getSql();
                    //转为Statement对象 便于拼接操作
                    Statement statement = CCJSqlParserUtil.parse(originalSql);
                    //Expression sqlSegmentExp = CCJSqlParserUtil.parseCondExpression(sqlSegment);
                    if(statement instanceof Select){
                        SelectBody selectBody = ((Select)statement).getSelectBody();
                        if(selectBody instanceof PlainSelect){
                            Expression originalWhere = ((PlainSelect) selectBody).getWhere();
                            Expression where = dataPermissionWhere(originalWhere, columnName, operator, value);

                            ((PlainSelect) selectBody).setWhere(where);
                        }
                    }

                    String sql = statement.toString();
                    // 反射获取boundSql 并设置sql属性
                    MetaObject newBoundSql = SystemMetaObject.forObject(boundSql);
                    newBoundSql.setValue("sql", sql);
                }
            }
        }

        return invocation.proceed();
    }

    private Expression dataPermissionWhere(Expression where, String columnName, String operator, String value){
        Column column = new Column(columnName);
        Expression dataPmsExp = null;
        switch (operator){
            case "=":
                dataPmsExp = new EqualsTo(column, new LongValue(value));
                break;
            case "in":
                List<Long> items = Arrays.asList(1L,2L,3L);
                List<Expression> values = items.stream().map(LongValue::new).collect(Collectors.toList());
                ExpressionList expressionList = new ExpressionList(values);
                dataPmsExp = new InExpression(column, expressionList);
                break;
            case ">=":
                dataPmsExp = new GreaterThanEquals();
                ((GreaterThanEquals)dataPmsExp).withLeftExpression(column);
                ((GreaterThanEquals)dataPmsExp).withRightExpression(new LongValue(value));
                break;
            case "<=":
                dataPmsExp = new MinorThanEquals();
                ((MinorThanEquals)dataPmsExp).withLeftExpression(column);
                ((MinorThanEquals)dataPmsExp).withRightExpression(new LongValue(value));
                break;
            case "like":
                dataPmsExp = new LikeExpression();
                ((LikeExpression)dataPmsExp).withLeftExpression(column);
                ((LikeExpression)dataPmsExp).withRightExpression(new StringValue(value));
                break;
            default:
                break;
        }

        if (where != null) {
            return new AndExpression(where, dataPmsExp);
        }else{
            return dataPmsExp;
        }
    }

    @Override
    public Object plugin(Object target) {
        // 提前拦截判断对象类型 以提高性能
        //if(target instanceof Executor){
        //    return Plugin.wrap(target, this);
        //}
        //return target;

        System.out.println("plugin target Type: " + target.getClass());

        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
