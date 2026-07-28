package com.jsnjfz.manage.config;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationConfigHardeningTest {

    @Test
    void requiresExplicitDatabaseAndCookieSecurityConfiguration() throws IOException {
        String yaml = classpathText("/application.yml");

        assertTrue(yaml.contains("url: ${DB_URL}"));
        assertTrue(yaml.contains("username: ${DB_USERNAME}"));
        assertTrue(yaml.contains("password: ${DB_PASSWORD}"));
        assertTrue(yaml.contains("secure-cookie: ${GUNS_SECURE_COOKIE}"));
        assertFalse(yaml.contains("DB_USERNAME:root"));
        assertFalse(yaml.contains("DB_PASSWORD:"));
    }

    private String classpathText(String path) throws IOException {
        try (InputStream input = getClass().getResourceAsStream(path)) {
            assertNotNull(input);
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
