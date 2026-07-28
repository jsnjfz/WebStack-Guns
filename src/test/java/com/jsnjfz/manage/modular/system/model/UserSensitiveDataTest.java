package com.jsnjfz.manage.modular.system.model;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Test
    void doesNotExposePasswordMaterialInJson() throws Exception {
        User user = new User();
        user.setAccount("admin");
        user.setPassword("password-material");
        user.setSalt("salt-material");

        String json = new ObjectMapper().writeValueAsString(user);

        assertFalse(json.contains("password"));
        assertFalse(json.contains("salt"));
        assertFalse(json.contains("password-material"));
        assertFalse(json.contains("salt-material"));
    }
}
