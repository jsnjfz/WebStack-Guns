package com.jsnjfz.manage.core.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UploadRateLimitServiceTest {

    @Test
    void limitsUploadsPerUserAndRemoteAddress() {
        UploadRateLimitService service = new UploadRateLimitService();

        for (int i = 0; i < UploadRateLimitService.MAX_UPLOADS_PER_WINDOW; i++) {
            assertTrue(service.tryAcquire(7, "127.0.0.1"));
        }
        assertFalse(service.tryAcquire(7, "127.0.0.1"));
        assertTrue(service.tryAcquire(8, "127.0.0.1"));
        assertFalse(service.tryAcquire(7, "127.0.0.2"));
    }

    @Test
    void rejectsMissingUserIdentity() {
        assertFalse(new UploadRateLimitService().tryAcquire(null, "127.0.0.1"));
    }
}
