package com.jsnjfz.manage.core.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 单实例图片上传限速，避免普通登录账号持续占用磁盘与图片解码资源。
 */
@Component
public class UploadRateLimitService {

    static final int MAX_UPLOADS_PER_WINDOW = 20;
    static final int MAX_UPLOADS_PER_REMOTE_WINDOW = 100;
    private static final long WINDOW_MILLIS = 24L * 60 * 60 * 1000;
    private static final int MAX_ENTRIES = 10_000;

    private final Map<String, UploadWindow> windows = new ConcurrentHashMap<>();
    private final AtomicLong operations = new AtomicLong();

    public boolean tryAcquire(Integer userId, String remoteAddress) {
        if (userId == null) {
            return false;
        }
        cleanupIfNeeded();
        long now = System.currentTimeMillis();
        boolean userAllowed = tryAcquire("user:" + userId, MAX_UPLOADS_PER_WINDOW, now);
        if (!userAllowed) {
            return false;
        }
        return tryAcquire(
                "remote:" + (remoteAddress == null ? "unknown" : remoteAddress),
                MAX_UPLOADS_PER_REMOTE_WINDOW,
                now);
    }

    private boolean tryAcquire(String key, int maximumUploads, long now) {
        AtomicBoolean allowed = new AtomicBoolean(false);
        windows.compute(key, (ignored, current) -> {
            if (current == null || now >= current.expiresAt) {
                allowed.set(true);
                return new UploadWindow(1, now + WINDOW_MILLIS);
            }
            if (current.uploads >= maximumUploads) {
                return current;
            }
            allowed.set(true);
            return new UploadWindow(current.uploads + 1, current.expiresAt);
        });
        return allowed.get();
    }

    private void cleanupIfNeeded() {
        if (operations.incrementAndGet() % 256 != 0 && windows.size() <= MAX_ENTRIES) {
            return;
        }
        long now = System.currentTimeMillis();
        for (Map.Entry<String, UploadWindow> entry : windows.entrySet()) {
            if (now >= entry.getValue().expiresAt) {
                windows.remove(entry.getKey(), entry.getValue());
            }
        }
        if (windows.size() > MAX_ENTRIES) {
            windows.clear();
        }
    }

    private static final class UploadWindow {
        private final int uploads;
        private final long expiresAt;

        private UploadWindow(int uploads, long expiresAt) {
            this.uploads = uploads;
            this.expiresAt = expiresAt;
        }
    }
}
