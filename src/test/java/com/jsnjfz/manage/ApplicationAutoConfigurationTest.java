package com.jsnjfz.manage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationAutoConfigurationTest {

    @Test
    void explicitlyDisablesLegacyFastjsonRedisAndDatabaseInitializers() {
        SpringBootApplication annotation =
                WebstackGunsApplication.class.getAnnotation(SpringBootApplication.class);
        Set<String> excluded = Set.copyOf(Arrays.asList(annotation.excludeName()));

        assertTrue(excluded.contains("cn.stylefeng.roses.core.config.FastjsonAutoConfiguration"));
        assertTrue(excluded.contains("cn.stylefeng.roses.core.config.RedisAutoConfiguration"));
        assertTrue(excluded.contains("cn.stylefeng.roses.core.config.DbInitializerAutoConfiguration"));
    }
}
