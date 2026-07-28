package com.jsnjfz.manage.core.security;

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
    private static final int MIN_NEW_PASSWORD_LENGTH = 12;
    private static final int MAX_NEW_PASSWORD_LENGTH = 128;
    private static final int TEMPORARY_PASSWORD_LENGTH = 20;
    private static final char[] UPPERCASE = "ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWERCASE = "abcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final char[] DIGITS = "23456789".toCharArray();
    private static final char[] SYMBOLS = "!@#$%*-_=+".toCharArray();
    private static final char[] TEMPORARY_PASSWORD_ALPHABET =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%*-_=+".toCharArray();

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

    /**
     * 生成只在重置响应中返回一次的随机临时密码。
     */
    public String generateTemporaryPassword() {
        char[] password = new char[TEMPORARY_PASSWORD_LENGTH];
        password[0] = randomCharacter(UPPERCASE);
        password[1] = randomCharacter(LOWERCASE);
        password[2] = randomCharacter(DIGITS);
        password[3] = randomCharacter(SYMBOLS);
        for (int i = 4; i < password.length; i++) {
            password[i] = randomCharacter(TEMPORARY_PASSWORD_ALPHABET);
        }
        for (int i = password.length - 1; i > 0; i--) {
            int swapIndex = secureRandom.nextInt(i + 1);
            char current = password[i];
            password[i] = password[swapIndex];
            password[swapIndex] = current;
        }
        return new String(password);
    }

    /**
     * 新建账号、主动改密使用的密码策略。旧摘要登录迁移不走此校验，避免锁死历史账号。
     */
    public boolean isAcceptableNewPassword(String rawPassword) {
        return rawPassword != null
                && rawPassword.length() >= MIN_NEW_PASSWORD_LENGTH
                && rawPassword.length() <= MAX_NEW_PASSWORD_LENGTH;
    }

    public boolean matches(String rawPassword, String encodedPassword, String legacySalt) {
        if (rawPassword == null || encodedPassword == null || rawPassword.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }
        if (!isModern(encodedPassword)) {
            return legacySalt != null && MessageDigest.isEqual(
                    legacyHash(rawPassword, legacySalt).getBytes(StandardCharsets.US_ASCII),
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

    private char randomCharacter(char[] alphabet) {
        return alphabet[secureRandom.nextInt(alphabet.length)];
    }

    private String legacyHash(String rawPassword, String legacySalt) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] saltDigest = md5.digest(legacySalt.getBytes(StandardCharsets.UTF_8));
            md5.update(saltDigest);
            byte[] hash = md5.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            for (int i = 1; i < 1024; i++) {
                hash = md5.digest(hash);
            }
            return toHex(hash);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("当前 JDK 不支持旧密码迁移所需的 MD5", e);
        }
    }

    private String toHex(byte[] bytes) {
        char[] hex = new char[bytes.length * 2];
        char[] digits = "0123456789abcdef".toCharArray();
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xff;
            hex[i * 2] = digits[value >>> 4];
            hex[i * 2 + 1] = digits[value & 0x0f];
        }
        return new String(hex);
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
