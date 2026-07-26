/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jsnjfz.manage.core.shiro;

import com.jsnjfz.manage.core.security.PasswordCredentialsMatcher;
import com.jsnjfz.manage.core.security.PasswordService;
import com.jsnjfz.manage.core.shiro.service.UserAuthService;
import com.jsnjfz.manage.core.shiro.service.impl.UserAuthServiceServiceImpl;
import com.jsnjfz.manage.modular.system.model.User;
import cn.stylefeng.roses.core.util.ToolUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShiroDbRealm extends AuthorizingRealm {

    private final PasswordService passwordService;

    public ShiroDbRealm(PasswordService passwordService) {
        this.passwordService = passwordService;
        super.setCredentialsMatcher(new PasswordCredentialsMatcher(passwordService));
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        UserAuthService shiroFactory = UserAuthServiceServiceImpl.me();
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        // UserAuthService#user 在账号不存在/被冻结时是直接抛异常而非返回 null，
        // 因此必须在这里补偿耗时：否则「账号不存在」会在几毫秒内返回，而
        // 「密码错误」要跑完 PBKDF2（约 240ms），仅凭响应时间即可枚举用户名
        // （CVE-2026-23901）。实测未补偿时两者差 239ms / 97.7%。
        User user;
        try {
            user = shiroFactory.user(token.getUsername());
        } catch (AuthenticationException e) {
            passwordService.burnTime(new String(token.getPassword()));
            throw e;
        }
        if (user == null) {
            passwordService.burnTime(new String(token.getPassword()));
            return null;
        }
        ShiroUser shiroUser = shiroFactory.shiroUser(user);
        return shiroFactory.info(shiroUser, user, super.getName());
    }

    /**
     * 权限认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserAuthService shiroFactory = UserAuthServiceServiceImpl.me();
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        List<Integer> roleList = shiroUser.getRoleList();

        Set<String> permissionSet = new HashSet<>();
        Set<String> roleNameSet = new HashSet<>();

        for (Integer roleId : roleList) {
            List<String> permissions = shiroFactory.findPermissionsByRoleId(roleId);
            if (permissions != null) {
                for (String permission : permissions) {
                    if (ToolUtil.isNotEmpty(permission)) {
                        permissionSet.add(permission);
                    }
                }
            }
            String roleName = shiroFactory.findRoleNameByRoleId(roleId);
            roleNameSet.add(roleName);
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionSet);
        info.addRoles(roleNameSet);
        return info;
    }

}
