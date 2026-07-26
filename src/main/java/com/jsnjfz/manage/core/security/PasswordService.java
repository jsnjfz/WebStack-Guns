package com.jsnjfz.manage.core.security;

import com.jsnjfz.manage.core.shiro.ShiroKit;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 新密码使用 PBKDF2；旧的带盐 MD5 仅用于登录时兼容和渐进迁移。
 */
@Component
public class PasswordService {

    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 210000;
    private static final int SALT_BYTES = 16;
    private static final int HASH_BITS = 256;
    private static final int MAX_PASSWORD_LENGTH = 1024;

    private final SecureRandom secureRandom = new SecureRandom();
    private final String dummyHash = encode("timing-mitigation-only");

    public String encode(String rawPassword) {
        validatePassword(rawPassword);
        byte[] salt = new byte[SALT_BYTES];
        secureRandom.nextBytes(salt);
        byte[] hash = derive(rawPassword.toCharArray(), salt, ITERATIONS);
        return PREFIX + "$" + ITERATIONS + "$"
                + Base64.getEncoder().withoutPadding().encodeToString(salt) + "$"
                + Base64.getEncoder().withoutPadding().encodeToString(hash);
    }

    public boolean matches(String rawPassword, String encodedPassword, String legacySalt) {
        if (rawPassword == null || encodedPassword == null || rawPassword.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }
        if (!isModern(encodedPassword)) {
            return legacySalt != null && MessageDigest.isEqual(
                    ShiroKit.md5(rawPassword, legacySalt).getBytes(StandardCharsets.US_ASCII),
                    encodedPassword.getBytes(StandardCharsets.US_ASCII));
        }
        try {
            String[] parts = encodedPassword.split("\\$");
            if (parts.length != 4) {
                return false;
            }
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            if (iterations < 100000 || iterations > 1000000
                    || salt.length < 16 || salt.length > 64
                    || expected.length < 16 || expected.length > 64) {
                return false;
            }
            byte[] actual = derive(rawPassword.toCharArray(), salt, iterations);
            return MessageDigest.isEqual(actual, expected);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 用户不存在时也执行一次同成本的 PBKDF2，降低账号枚举的时序差异。
     * 旧 MD5 账号在迁移前也执行同一次计算，避免暴露其存储格式。
     */
    public boolean matchesOrBurn(String rawPassword, String encodedPassword, String legacySalt) {
        if (encodedPassword == null) {
            burnTime(rawPassword);
            return false;
        }
        boolean matched = matches(rawPassword, encodedPassword, legacySalt);
        if (!isModern(encodedPassword)) {
            burnTime(rawPassword);
        }
        return matched;
    }

    public void burnTime(String rawPassword) {
        String candidate = rawPassword == null ? "" : rawPassword;
        matches(candidate, dummyHash, null);
    }

    public boolean isModern(String encodedPassword) {
        return encodedPassword != null && encodedPassword.startsWith(PREFIX + "$");
    }

    private byte[] derive(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, HASH_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("当前 JDK 不支持 PBKDF2WithHmacSHA256", e);
        } finally {
            spec.clearPassword();
        }
    }

    private void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (rawPassword.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("密码长度不能超过 " + MAX_PASSWORD_LENGTH + " 个字符");
        }
    }
}
