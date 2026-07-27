package com.jsnjfz.manage.core.datascope;

import cn.hutool.core.collection.CollectionUtil;
import cn.stylefeng.roses.core.datascope.DataScope;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * JDK 17 兼容的数据范围插件。
 *
 * <p>旧实现通过反射读取 {@link Proxy} 的受保护字段，JDK 17 模块边界会拒绝该访问。
 * 此实现使用公开的 {@link Proxy#getInvocationHandler(Object)} 解包 MyBatis 插件代理。</p>
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare",
                args = {Connection.class, Integer.class})
})
public class Jdk17DataScopeInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = unwrap(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement =
                (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (mappedStatement.getSqlCommandType() != SqlCommandType.SELECT) {
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        DataScope dataScope = findDataScopeObject(boundSql.getParameterObject());
        if (dataScope == null) {
            return invocation.proceed();
        }

        String deptIds = CollectionUtil.join(dataScope.getDeptIds(), ",");
        String scopedSql = "select * from (" + boundSql.getSql()
                + ") temp_data_scope where temp_data_scope."
                + dataScope.getScopeName() + " in (" + deptIds + ")";
        metaObject.setValue("delegate.boundSql.sql", scopedSql);
        return invocation.proceed();
    }

    private StatementHandler unwrap(Object target) {
        Object current = target;
        while (Proxy.isProxyClass(current.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(current);
            if (!(handler instanceof Plugin)) {
                break;
            }
            current = SystemMetaObject.forObject(handler).getValue("target");
        }
        return (StatementHandler) current;
    }

    private DataScope findDataScopeObject(Object parameterObject) {
        if (parameterObject instanceof DataScope) {
            return (DataScope) parameterObject;
        }
        if (parameterObject instanceof Map) {
            for (Object value : ((Map<?, ?>) parameterObject).values()) {
                if (value instanceof DataScope) {
                    return (DataScope) value;
                }
            }
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 无配置项
    }
}
