package com.jsnjfz.manage.modular.system.dao;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogMapperSafetyTest {

    @Test
    void logMappersUseFixedSortColumnsInsteadOfTextSubstitution() throws IOException {
        assertSafeMapper("/com/jsnjfz/manage/modular/system/dao/mapping/OperationLogMapper.xml");
        assertSafeMapper("/com/jsnjfz/manage/modular/system/dao/mapping/LoginLogMapper.xml");
    }

    @Test
    void logMappersRenderOnlyFixedColumnsAndKeepDescendingDefault() throws IOException {
        assertEquals(
                "select * from sys_operation_log order by createtime DESC",
                renderedSql(
                        "/com/jsnjfz/manage/modular/system/dao/mapping/OperationLogMapper.xml",
                        "com.jsnjfz.manage.modular.system.dao.OperationLogMapper.getOperationLogs",
                        null,
                        true));
        assertEquals(
                "select * from sys_operation_log order by message ASC",
                renderedSql(
                        "/com/jsnjfz/manage/modular/system/dao/mapping/OperationLogMapper.xml",
                        "com.jsnjfz.manage.modular.system.dao.OperationLogMapper.getOperationLogs",
                        "message",
                        true));
        assertEquals(
                "select * from sys_login_log order by createtime DESC",
                renderedSql(
                        "/com/jsnjfz/manage/modular/system/dao/mapping/LoginLogMapper.xml",
                        "com.jsnjfz.manage.modular.system.dao.LoginLogMapper.getLoginLogs",
                        "not_allowed",
                        true));
    }

    private void assertSafeMapper(String path) throws IOException {
        String mapper;
        try (InputStream input = getClass().getResourceAsStream(path)) {
            assertNotNull(input);
            mapper = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
        assertFalse(mapper.contains("${orderByField}"));
        assertTrue(mapper.contains("<choose>"));
    }

    private String renderedSql(String path, String statementId, String orderByField, boolean isAsc)
            throws IOException {
        Configuration configuration = new Configuration();
        try (InputStream input = getClass().getResourceAsStream(path)) {
            assertNotNull(input);
            new XMLMapperBuilder(input, configuration, path, configuration.getSqlFragments()).parse();
        }
        MappedStatement statement = configuration.getMappedStatement(statementId);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderByField", orderByField);
        parameters.put("isAsc", isAsc);
        return statement.getBoundSql(parameters).getSql().replaceAll("\\s+", " ").trim();
    }
}
