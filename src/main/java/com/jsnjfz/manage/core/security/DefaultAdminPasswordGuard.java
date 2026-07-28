package com.jsnjfz.manage.core.security;

import com.jsnjfz.manage.config.properties.GunsProperties;
import com.jsnjfz.manage.core.common.constant.Const;
import com.jsnjfz.manage.modular.system.model.User;
import com.jsnjfz.manage.modular.system.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * 阻止初始化 SQL 中的历史默认管理员密码进入可用运行态。
 */
@Component
public class DefaultAdminPasswordGuard implements SmartInitializingSingleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAdminPasswordGuard.class);
    static final String BOOTSTRAP_REQUIRED_PASSWORD = "!BOOTSTRAP_REQUIRED!";
    private static final String LEGACY_DEFAULT_PASSWORD = "111111";
    private static final int MIN_BOOTSTRAP_PASSWORD_LENGTH = 12;
    private static final int MAX_BOOTSTRAP_PASSWORD_LENGTH = 128;

    private final IUserService userService;
    private final PasswordService passwordService;
    private final GunsProperties gunsProperties;

    public DefaultAdminPasswordGuard(IUserService userService,
                                     PasswordService passwordService,
                                     GunsProperties gunsProperties) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.gunsProperties = gunsProperties;
    }

    @Override
    public void afterSingletonsInstantiated() {
        User admin = userService.selectById(Const.ADMIN_ID);
        if (admin == null) {
            gunsProperties.setBootstrapAdminPassword(null);
            return;
        }
        boolean requiresBootstrap = BOOTSTRAP_REQUIRED_PASSWORD.equals(admin.getPassword())
                || passwordService.matches(
                        LEGACY_DEFAULT_PASSWORD, admin.getPassword(), admin.getSalt());
        if (!requiresBootstrap) {
            gunsProperties.setBootstrapAdminPassword(null);
            return;
        }

        String bootstrapPassword = gunsProperties.getBootstrapAdminPassword();
        validateBootstrapPassword(bootstrapPassword);

        admin.setPassword(passwordService.encode(bootstrapPassword));
        admin.setSalt("");
        if (!userService.updateById(admin)) {
            throw new IllegalStateException("管理员默认密码轮换失败，应用已拒绝启动");
        }
        gunsProperties.setBootstrapAdminPassword(null);
        LOGGER.info("检测到历史默认管理员密码，已使用部署环境提供的密码完成轮换");
    }

    private void validateBootstrapPassword(String bootstrapPassword) {
        if (bootstrapPassword == null
                || bootstrapPassword.trim().isEmpty()
                || bootstrapPassword.length() < MIN_BOOTSTRAP_PASSWORD_LENGTH
                || bootstrapPassword.length() > MAX_BOOTSTRAP_PASSWORD_LENGTH
                || LEGACY_DEFAULT_PASSWORD.equals(bootstrapPassword)) {
            throw new IllegalStateException(
                    "检测到历史默认管理员密码。必须设置 12 到 128 位的 "
                            + "GUNS_BOOTSTRAP_ADMIN_PASSWORD 后才能启动");
        }
    }
}
