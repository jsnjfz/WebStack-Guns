package com.jsnjfz.manage.config.web;

import com.jsnjfz.manage.core.security.PasswordService;
import com.jsnjfz.manage.core.shiro.ShiroDbRealm;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.junit.jupiter.api.Test;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class ShiroConfigTest {

    @Test
    void disablesRememberMeManager() {
        ShiroConfig config = new ShiroConfig();

        DefaultWebSecurityManager securityManager = config.securityManager(
                new MemoryConstrainedCacheManager(),
                new DefaultWebSessionManager(),
                new ShiroDbRealm(new PasswordService()));

        assertNull(securityManager.getRememberMeManager());
    }

    @Test
    void restrictsOptionalDiagnosticsAndApiDocsToAdministrators() {
        ShiroFilterFactoryBean filter =
                new ShiroConfig().shiroFilter(mock(DefaultWebSecurityManager.class));

        assertEquals("roles[administrator]",
                filter.getFilterChainDefinitionMap().get("/druid/**"));
        assertEquals("roles[administrator]",
                filter.getFilterChainDefinitionMap().get("/swagger-ui.html"));
        assertEquals("roles[administrator]",
                filter.getFilterChainDefinitionMap().get("/swagger-ui/**"));
        assertEquals("roles[administrator]",
                filter.getFilterChainDefinitionMap().get("/v2/api-docs"));
        assertEquals("roles[administrator]",
                filter.getFilterChainDefinitionMap().get("/v3/api-docs/**"));
    }
}
