package com.jsnjfz.manage.core.security;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 单实例登录失败限速。生产多实例部署时仍应在网关层增加统一限速。
 */
@Component
public class LoginAttemptService {

    private static final int MAX_FAILURES = 5;
    private static final long WINDOW_MILLIS = 5 * 60 * 1000L;
    private static final int MAX_ENTRIES = 10000;

    private final Map<String, AttemptWindow> attempts = new ConcurrentHashMap<>();
    private final AtomicLong operations = new AtomicLong();

    public boolean isAllowed(String remoteAddress, String username) {
        cleanupIfNeeded();
        AttemptWindow window = attempts.get(key(remoteAddress, username));
        return window == null || window.expired() || window.failures < MAX_FAILURES;
    }

    public void recordFailure(String remoteAddress, String username) {
        String key = key(remoteAddress, username);
        attempts.compute(key, (ignored, current) -> {
            if (current == null || current.expired()) {
                return new AttemptWindow(1, System.currentTimeMillis() + WINDOW_MILLIS);
            }
            return new AttemptWindow(current.failures + 1, current.expiresAt);
        });
        cleanupIfNeeded();
    }

    public void reset(String remoteAddress, String username) {
        attempts.remove(key(remoteAddress, username));
    }

    private String key(String remoteAddress, String username) {
        String address = remoteAddress == null ? "unknown" : remoteAddress;
        String account = username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
        if (account.length() > 128) {
            account = account.substring(0, 128);
        }
        return address + '\n' + account;
    }

    private void cleanupIfNeeded() {
        if (operations.incrementAndGet() % 256 != 0 && attempts.size() <= MAX_ENTRIES) {
            return;
        }
        for (Map.Entry<String, AttemptWindow> entry : attempts.entrySet()) {
            if (entry.getValue().expired()) {
                attempts.remove(entry.getKey(), entry.getValue());
            }
        }
        if (attempts.size() > MAX_ENTRIES) {
            attempts.clear();
        }
    }

    private static final class AttemptWindow {
        private final int failures;
        private final long expiresAt;

        private AttemptWindow(int failures, long expiresAt) {
            this.failures = failures;
            this.expiresAt = expiresAt;
        }

        private boolean expired() {
            return System.currentTimeMillis() >= expiresAt;
        }
    }
}
