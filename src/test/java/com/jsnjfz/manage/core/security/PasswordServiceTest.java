package com.jsnjfz.manage.core.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordServiceTest {

    private final PasswordService passwordService = new PasswordService();

    @Test
    void supportsModernPasswordHashes() {
        String encoded = passwordService.encode("a-secure-password");

        assertTrue(passwordService.isModern(encoded));
        assertTrue(passwordService.matches("a-secure-password", encoded, null));
        assertFalse(passwordService.matches("wrong-password", encoded, null));
    }

    @Test
    void acceptsTheLegacyAdminPasswordForMigration() {
        assertTrue(passwordService.matchesOrBurn(
                "111111", "ecfadcde9305f8891bcfe5a1e28c253e", "8pgby"));
        assertFalse(passwordService.matchesOrBurn(
                "111112", "ecfadcde9305f8891bcfe5a1e28c253e", "8pgby"));
    }

    @Test
    void generatesStrongRandomTemporaryPasswords() {
        String first = passwordService.generateTemporaryPassword();
        String second = passwordService.generateTemporaryPassword();

        assertEquals(20, first.length());
        assertTrue(first.chars().anyMatch(Character::isUpperCase));
        assertTrue(first.chars().anyMatch(Character::isLowerCase));
        assertTrue(first.chars().anyMatch(Character::isDigit));
        assertTrue(first.chars().anyMatch(character -> "!@#$%*-_=+".indexOf(character) >= 0));
        assertNotEquals(first, second);
    }

    @Test
    void enforcesLengthForNewPasswords() {
        assertFalse(passwordService.isAcceptableNewPassword("short"));
        assertTrue(passwordService.isAcceptableNewPassword("twelve-chars"));
        assertFalse(passwordService.isAcceptableNewPassword("x".repeat(129)));
    }
}
