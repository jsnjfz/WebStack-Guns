package com.jsnjfz.manage.core.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * 同时接受 PBKDF2 新格式和存量 MD5，存量密码会在成功登录后迁移。
 */
public class PasswordCredentialsMatcher implements CredentialsMatcher {

    private final PasswordService passwordService;
    private final HashedCredentialsMatcher legacyMatcher;

    public PasswordCredentialsMatcher(PasswordService passwordService) {
        this.passwordService = passwordService;
        this.legacyMatcher = new HashedCredentialsMatcher();
        this.legacyMatcher.setHashAlgorithmName("MD5");
        this.legacyMatcher.setHashIterations(1024);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String stored = String.valueOf(info.getCredentials());
        if (!passwordService.isModern(stored)) {
            boolean matched = legacyMatcher.doCredentialsMatch(token, info);
            passwordService.burnTime(rawPassword(token));
            return matched;
        }
        return passwordService.matches(rawPassword(token), stored, null);
    }

    private String rawPassword(AuthenticationToken token) {
        Object submitted = token.getCredentials();
        return submitted instanceof char[]
                ? new String((char[]) submitted)
                : String.valueOf(submitted);
    }
}
