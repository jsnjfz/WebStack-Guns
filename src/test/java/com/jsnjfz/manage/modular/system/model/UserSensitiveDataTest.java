package com.jsnjfz.manage.modular.system.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UserSensitiveDataTest {

    @Test
    void doesNotExposePasswordMaterialInToString() {
        User user = new User();
        user.setPassword("password-material");
        user.setSalt("salt-material");

        String text = user.toString();

        assertFalse(text.contains("password-material"));
        assertFalse(text.contains("salt-material"));
    }
}
