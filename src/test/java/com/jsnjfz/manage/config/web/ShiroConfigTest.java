package com.jsnjfz.manage.config.web;

import com.jsnjfz.manage.core.security.PasswordService;
import com.jsnjfz.manage.core.shiro.ShiroDbRealm;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

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
}
