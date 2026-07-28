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
package com.jsnjfz.manage.modular.system.factory;

import com.jsnjfz.manage.modular.system.model.User;
import com.jsnjfz.manage.modular.system.transfer.UserDto;
import cn.stylefeng.roses.core.util.ToolUtil;
import org.springframework.beans.BeanUtils;

/**
 * 用户创建工厂
 *
 * @author fengshuonan
 * @date 2017-05-05 22:43
 */
public class UserFactory {

    public static User createUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        } else {
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            return user;
        }
    }

    public static User editUser(UserDto newUser, User oldUser) {
        return editUser(newUser, oldUser, true);
    }

    /**
     * 普通用户编辑自己的资料时，不允许通过请求参数改变所属部门。
     */
    public static User editOwnProfile(UserDto newUser, User oldUser) {
        return editUser(newUser, oldUser, false);
    }

    private static User editUser(UserDto newUser, User oldUser, boolean allowDepartmentChange) {
        if (newUser == null || oldUser == null) {
            return oldUser;
        } else {
            if (ToolUtil.isNotEmpty(newUser.getAvatar())) {
                oldUser.setAvatar(newUser.getAvatar());
            }
            if (ToolUtil.isNotEmpty(newUser.getName())) {
                oldUser.setName(newUser.getName());
            }
            if (ToolUtil.isNotEmpty(newUser.getBirthday())) {
                oldUser.setBirthday(newUser.getBirthday());
            }
            if (allowDepartmentChange && ToolUtil.isNotEmpty(newUser.getDeptid())) {
                oldUser.setDeptid(newUser.getDeptid());
            }
            if (ToolUtil.isNotEmpty(newUser.getSex())) {
                oldUser.setSex(newUser.getSex());
            }
            if (ToolUtil.isNotEmpty(newUser.getEmail())) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (ToolUtil.isNotEmpty(newUser.getPhone())) {
                oldUser.setPhone(newUser.getPhone());
            }
            return oldUser;
        }
    }
}
