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

    static final int MAX_FAILURES_PER_ACCOUNT_AND_REMOTE = 5;
    static final int MAX_FAILURES_PER_ACCOUNT = 20;
    static final int MAX_FAILURES_PER_REMOTE = 50;
    private static final long WINDOW_MILLIS = 5 * 60 * 1000L;
    private static final int MAX_ENTRIES = 10000;

    private final Map<String, AttemptWindow> attempts = new ConcurrentHashMap<>();
    private final AtomicLong operations = new AtomicLong();

    public boolean isAllowed(String remoteAddress, String username) {
        cleanupIfNeeded();
        String address = normalizeRemoteAddress(remoteAddress);
        String account = normalizeAccount(username);
        String pairKey = pairKey(address, account);
        String accountKey = accountKey(account);
        String remoteKey = remoteKey(address);
        if (!hasCapacityFor(pairKey, accountKey, remoteKey)) {
            return false;
        }
        long now = System.currentTimeMillis();
        return isAllowed(pairKey, MAX_FAILURES_PER_ACCOUNT_AND_REMOTE, now)
                && isAllowed(accountKey, MAX_FAILURES_PER_ACCOUNT, now)
                && isAllowed(remoteKey, MAX_FAILURES_PER_REMOTE, now);
    }

    public void recordFailure(String remoteAddress, String username) {
        String address = normalizeRemoteAddress(remoteAddress);
        String account = normalizeAccount(username);
        long now = System.currentTimeMillis();
        increment(pairKey(address, account), now);
        increment(accountKey(account), now);
        increment(remoteKey(address), now);
        cleanupIfNeeded();
    }

    public void reset(String remoteAddress, String username) {
        String address = normalizeRemoteAddress(remoteAddress);
        String account = normalizeAccount(username);
        attempts.remove(pairKey(address, account));
        attempts.remove(accountKey(account));
    }

    private boolean isAllowed(String key, int maximumFailures, long now) {
        AttemptWindow window = attempts.get(key);
        return window == null || window.expired(now) || window.failures < maximumFailures;
    }

    private void increment(String key, long now) {
        attempts.compute(key, (ignored, current) -> {
            if (current == null && attempts.size() >= MAX_ENTRIES) {
                return null;
            }
            if (current == null || current.expired(now)) {
                return new AttemptWindow(1, now + WINDOW_MILLIS);
            }
            return new AttemptWindow(current.failures + 1, current.expiresAt);
        });
    }

    private boolean hasCapacityFor(String... keys) {
        int missingKeys = 0;
        for (String key : keys) {
            if (!attempts.containsKey(key)) {
                missingKeys++;
            }
        }
        return attempts.size() <= MAX_ENTRIES - missingKeys;
    }

    private String normalizeRemoteAddress(String remoteAddress) {
        String address = remoteAddress == null ? "unknown" : remoteAddress.trim();
        return address.length() > 128 ? address.substring(0, 128) : address;
    }

    private String normalizeAccount(String username) {
        String account = username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
        return account.length() > 128 ? account.substring(0, 128) : account;
    }

    private String pairKey(String address, String account) {
        return "pair\n" + address + '\n' + account;
    }

    private String accountKey(String account) {
        return "account\n" + account;
    }

    private String remoteKey(String address) {
        return "remote\n" + address;
    }

    private void cleanupIfNeeded() {
        if (operations.incrementAndGet() % 256 != 0) {
            return;
        }
        long now = System.currentTimeMillis();
        for (Map.Entry<String, AttemptWindow> entry : attempts.entrySet()) {
            if (entry.getValue().expired(now)) {
                attempts.remove(entry.getKey(), entry.getValue());
            }
        }
    }

    private static final class AttemptWindow {
        private final int failures;
        private final long expiresAt;

        private AttemptWindow(int failures, long expiresAt) {
            this.failures = failures;
            this.expiresAt = expiresAt;
        }

        private boolean expired(long now) {
            return now >= expiresAt;
        }
    }
}
