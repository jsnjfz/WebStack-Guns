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
package com.jsnjfz.manage.modular.api;

import com.jsnjfz.manage.core.util.JwtTokenUtil;
import com.jsnjfz.manage.core.security.PasswordService;
import com.jsnjfz.manage.core.security.LoginAttemptService;
import com.jsnjfz.manage.core.common.constant.state.ManagerStatus;
import com.jsnjfz.manage.modular.system.dao.UserMapper;
import com.jsnjfz.manage.modular.system.model.User;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ErrorResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 接口控制器提供
 *
 * @author stylefeng
 * @Date 2018/7/20 23:39
 */
@RestController
@RequestMapping("/gunsApi")
public class ApiController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    /**
     * api登录接口，通过账号密码获取token
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public Object auth(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       HttpServletRequest request) {

        //获取数据库中的账号密码，准备比对
        String remoteAddress = request.getRemoteAddr();
        if (!loginAttemptService.isAllowed(remoteAddress, username)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ErrorResponseData(429, "登录失败次数过多，请稍后再试"));
        }
        User user = userMapper.getByAccount(username);
        boolean credentialsMatch = passwordService.matchesOrBurn(
                password,
                user == null ? null : user.getPassword(),
                user == null ? null : user.getSalt());
        boolean passwordTrueFlag = credentialsMatch
                && user != null
                && user.getStatus() != null
                && ManagerStatus.OK.getCode() == user.getStatus();

        if (passwordTrueFlag) {
            loginAttemptService.reset(remoteAddress, username);
            if (!passwordService.isModern(user.getPassword())) {
                user.setPassword(passwordService.encode(password));
                user.setSalt("");
                user.updateById();
            }
            HashMap<String, Object> result = new HashMap<>();
            result.put("token", jwtTokenUtil.generateToken(String.valueOf(user.getId())));
            return result;
        } else {
            loginAttemptService.recordFailure(remoteAddress, username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseData(401, "账号密码错误！"));
        }
    }

    /**
     * 测试接口是否走鉴权
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Object test() {
        return SUCCESS_TIP;
    }

}
