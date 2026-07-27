package com.jsnjfz.manage.core.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import java.nio.charset.StandardCharsets;

/**
 * 同时接受 PBKDF2 新格式和存量 MD5，存量密码会在成功登录后迁移。
 */
public class PasswordCredentialsMatcher implements CredentialsMatcher {

    private final PasswordService passwordService;

    public PasswordCredentialsMatcher(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String stored = String.valueOf(info.getCredentials());
        if (!passwordService.isModern(stored)) {
            return passwordService.matchesOrBurn(
                    rawPassword(token),
                    stored,
                    legacySalt(info));
        }
        return passwordService.matches(rawPassword(token), stored, null);
    }

    private String legacySalt(AuthenticationInfo info) {
        if (!(info instanceof SaltedAuthenticationInfo)) {
            return null;
        }
        SaltedAuthenticationInfo saltedInfo = (SaltedAuthenticationInfo) info;
        return saltedInfo.getCredentialsSalt() == null
                ? null
                : new String(saltedInfo.getCredentialsSalt().getBytes(), StandardCharsets.UTF_8);
    }

    private String rawPassword(AuthenticationToken token) {
        Object submitted = token.getCredentials();
        return submitted instanceof char[]
                ? new String((char[]) submitted)
                : String.valueOf(submitted);
    }
}
