package com.jsnjfz.manage.modular.system.controller;

import com.jsnjfz.manage.core.common.annotion.Permission;
import com.jsnjfz.manage.core.common.constant.Const;
import com.jsnjfz.manage.modular.system.model.Category;
import com.jsnjfz.manage.modular.system.model.Notice;
import com.jsnjfz.manage.modular.system.model.Site;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ControllerPermissionTest {

    @Test
    void restrictsSiteAndCategoryWritesToAdministrators() throws Exception {
        assertAdminOnly(SiteController.class, "add", Site.class);
        assertAdminOnly(SiteController.class, "update", Site.class);
        assertAdminOnly(SiteController.class, "delete", Integer.class);
        assertAdminOnly(CategoryController.class, "add", Category.class);
        assertAdminOnly(CategoryController.class, "update", Category.class);
        assertAdminOnly(CategoryController.class, "delete", Integer.class);
    }

    @Test
    void enforcesConfiguredPermissionsForNoticeWrites() throws Exception {
        assertPermissionPresent(NoticeController.class, "add", Notice.class);
        assertPermissionPresent(NoticeController.class, "update", Notice.class);
        assertPermissionPresent(NoticeController.class, "delete", Integer.class);
    }

    private void assertAdminOnly(Class<?> controller, String methodName, Class<?> parameter)
            throws Exception {
        Permission permission = permission(controller, methodName, parameter);
        assertNotNull(permission);
        assertArrayEquals(new String[]{Const.ADMIN_NAME}, permission.value());
    }

    private void assertPermissionPresent(Class<?> controller, String methodName, Class<?> parameter)
            throws Exception {
        assertNotNull(permission(controller, methodName, parameter));
    }

    private Permission permission(Class<?> controller, String methodName, Class<?> parameter)
            throws Exception {
        Method method = controller.getDeclaredMethod(methodName, parameter);
        return method.getAnnotation(Permission.class);
    }
}
