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

        for (int i = 0; i < LoginAttemptService.MAX_FAILURES_PER_ACCOUNT_AND_REMOTE; i++) {
            assertTrue(service.isAllowed(address, username));
            service.recordFailure(address, username);
        }
        assertFalse(service.isAllowed(address, username));

        service.reset(address, username);
        assertTrue(service.isAllowed(address, username));
    }

    @Test
    void blocksDistributedAttemptsAgainstOneAccount() {
        LoginAttemptService service = new LoginAttemptService();

        for (int i = 0; i < LoginAttemptService.MAX_FAILURES_PER_ACCOUNT; i++) {
            String address = "192.0.2." + i;
            assertTrue(service.isAllowed(address, "admin"));
            service.recordFailure(address, "admin");
        }

        assertFalse(service.isAllowed("198.51.100.1", "admin"));
    }

    @Test
    void blocksOneRemoteThatRotatesAccountNames() {
        LoginAttemptService service = new LoginAttemptService();
        String address = "203.0.113.10";

        for (int i = 0; i < LoginAttemptService.MAX_FAILURES_PER_REMOTE; i++) {
            String username = "missing-" + i;
            assertTrue(service.isAllowed(address, username));
            service.recordFailure(address, username);
        }

        assertFalse(service.isAllowed(address, "another-account"));
    }
}
