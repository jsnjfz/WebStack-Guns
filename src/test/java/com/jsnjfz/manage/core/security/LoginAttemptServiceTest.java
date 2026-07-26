package com.jsnjfz.manage.core.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginAttemptServiceTest {

    @Test
    void blocksAfterFiveFailuresAndResetsAfterSuccess() {
        LoginAttemptService service = new LoginAttemptService();
        String address = "127.0.0.1";
        String username = "admin";

        for (int i = 0; i < 5; i++) {
            assertTrue(service.isAllowed(address, username));
            service.recordFailure(address, username);
        }
        assertFalse(service.isAllowed(address, username));

        service.reset(address, username);
        assertTrue(service.isAllowed(address, username));
    }
}
