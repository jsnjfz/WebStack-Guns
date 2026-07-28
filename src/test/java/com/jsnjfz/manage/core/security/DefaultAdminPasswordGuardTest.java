package com.jsnjfz.manage.core.security;

import com.jsnjfz.manage.config.properties.GunsProperties;
import com.jsnjfz.manage.core.common.constant.Const;
import com.jsnjfz.manage.modular.system.model.User;
import com.jsnjfz.manage.modular.system.service.IUserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultAdminPasswordGuardTest {

    private static final PasswordService PASSWORD_SERVICE = new PasswordService();

    @Test
    void refusesToStartWithHistoricalDefaultPasswordAndNoReplacement() {
        IUserService userService = mock(IUserService.class);
        GunsProperties properties = new GunsProperties();
        User admin = legacyDefaultAdmin();
        when(userService.selectById(Const.ADMIN_ID)).thenReturn(admin);
        DefaultAdminPasswordGuard guard =
                new DefaultAdminPasswordGuard(userService, PASSWORD_SERVICE, properties);

        assertThrows(IllegalStateException.class, guard::afterSingletonsInstantiated);
        verify(userService, never()).updateById(any(User.class));
    }

    @Test
    void rotatesHistoricalDefaultPasswordFromDeploymentConfiguration() {
        IUserService userService = mock(IUserService.class);
        GunsProperties properties = new GunsProperties();
        properties.setBootstrapAdminPassword("A-new-bootstrap-password-2026");
        User admin = legacyDefaultAdmin();
        when(userService.selectById(Const.ADMIN_ID)).thenReturn(admin);
        when(userService.updateById(any(User.class))).thenReturn(true);
        DefaultAdminPasswordGuard guard =
                new DefaultAdminPasswordGuard(userService, PASSWORD_SERVICE, properties);

        guard.afterSingletonsInstantiated();

        assertTrue(PASSWORD_SERVICE.isModern(admin.getPassword()));
        assertTrue(PASSWORD_SERVICE.matches(
                "A-new-bootstrap-password-2026", admin.getPassword(), null));
        assertEquals("", admin.getSalt());
        verify(userService).updateById(admin);
    }

    @Test
    void activatesFreshAdministratorOnlyWithDeploymentPassword() {
        IUserService userService = mock(IUserService.class);
        GunsProperties properties = new GunsProperties();
        properties.setBootstrapAdminPassword("A-new-bootstrap-password-2026");
        User admin = new User();
        admin.setPassword(DefaultAdminPasswordGuard.BOOTSTRAP_REQUIRED_PASSWORD);
        admin.setSalt("");
        when(userService.selectById(Const.ADMIN_ID)).thenReturn(admin);
        when(userService.updateById(any(User.class))).thenReturn(true);
        DefaultAdminPasswordGuard guard =
                new DefaultAdminPasswordGuard(userService, PASSWORD_SERVICE, properties);

        guard.afterSingletonsInstantiated();

        assertTrue(PASSWORD_SERVICE.matches(
                "A-new-bootstrap-password-2026", admin.getPassword(), null));
        verify(userService).updateById(admin);
    }

    @Test
    void leavesAnAlreadyChangedPasswordUntouched() {
        IUserService userService = mock(IUserService.class);
        GunsProperties properties = new GunsProperties();
        User admin = new User();
        admin.setPassword(PASSWORD_SERVICE.encode("already-changed-password"));
        admin.setSalt("");
        when(userService.selectById(Const.ADMIN_ID)).thenReturn(admin);
        DefaultAdminPasswordGuard guard =
                new DefaultAdminPasswordGuard(userService, PASSWORD_SERVICE, properties);

        guard.afterSingletonsInstantiated();

        verify(userService, never()).updateById(any(User.class));
    }

    private User legacyDefaultAdmin() {
        User admin = new User();
        admin.setPassword("ecfadcde9305f8891bcfe5a1e28c253e");
        admin.setSalt("8pgby");
        return admin;
    }
}
