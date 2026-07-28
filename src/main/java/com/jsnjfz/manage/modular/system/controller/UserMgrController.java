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
package com.jsnjfz.manage.modular.system.controller;

import com.jsnjfz.manage.config.properties.GunsProperties;
import com.jsnjfz.manage.core.common.annotion.BussinessLog;
import com.jsnjfz.manage.core.common.annotion.Permission;
import com.jsnjfz.manage.core.common.constant.Const;
import com.jsnjfz.manage.core.common.constant.dictmap.UserDict;
import com.jsnjfz.manage.core.common.constant.factory.ConstantFactory;
import com.jsnjfz.manage.core.common.constant.state.ManagerStatus;
import com.jsnjfz.manage.core.common.exception.BizExceptionEnum;
import com.jsnjfz.manage.core.common.exception.InvalidUploadException;
import com.jsnjfz.manage.core.common.exception.UploadRateLimitException;
import com.jsnjfz.manage.core.log.LogObjectHolder;
import com.jsnjfz.manage.core.shiro.ShiroKit;
import com.jsnjfz.manage.core.shiro.ShiroUser;
import com.jsnjfz.manage.core.security.PasswordService;
import com.jsnjfz.manage.core.security.UploadRateLimitService;
import com.jsnjfz.manage.modular.system.factory.UserFactory;
import com.jsnjfz.manage.modular.system.model.User;
import com.jsnjfz.manage.modular.system.service.IUserService;
import com.jsnjfz.manage.modular.system.transfer.UserDto;
import com.jsnjfz.manage.modular.system.warpper.UserWarpper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

/**
 * 系统管理员控制器
 *
 * @author fengshuonan
 * @Date 2017年1月11日 下午1:08:17
 */
@Controller
@RequestMapping("/mgr")
public class UserMgrController extends BaseController {

    private static final long MAX_IMAGE_BYTES = 5L * 1024 * 1024;
    private static final long MAX_IMAGE_PIXELS = 16L * 1024 * 1024;
    private static final Set<String> IMAGE_EXTENSIONS =
            new HashSet<>(java.util.Arrays.asList("jpg", "jpeg", "png", "gif"));

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UploadRateLimitService uploadRateLimitService;

    private static String PREFIX = "/system/user/";

    @Autowired
    private GunsProperties gunsProperties;

    @Autowired
    private IUserService userService;

    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "user.html";
    }

    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("/user_add")
    public String addView() {
        return PREFIX + "user_add.html";
    }

    /**
     * 跳转到角色分配页面
     */
    //@RequiresPermissions("/mgr/role_assign")  //利用shiro自带的权限检查
    @Permission
    @RequestMapping("/role_assign/{userId}")
    public String roleAssign(@PathVariable Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        User user = this.userService.selectOne(new EntityWrapper<User>().eq("id", userId));
        model.addAttribute("userId", userId);
        model.addAttribute("userAccount", user.getAccount());
        return PREFIX + "user_roleassign.html";
    }

    /**
     * 跳转到编辑管理员页面
     */
    @Permission
    @RequestMapping("/user_edit/{userId}")
    public String userEdit(@PathVariable Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
        LogObjectHolder.me().set(user);
        return PREFIX + "user_edit.html";
    }

    /**
     * 跳转到查看用户详情页面
     */
    @RequestMapping("/user_info")
    public String userInfo(Model model) {
        Integer userId = ShiroKit.getUser().getId();
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        User user = this.userService.selectById(userId);
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
        LogObjectHolder.me().set(user);
        return PREFIX + "user_view.html";
    }

    /**
     * 跳转到修改密码界面
     */
    @RequestMapping("/user_chpwd")
    public String chPwd() {
        return PREFIX + "user_chpwd.html";
    }

    /**
     * 修改当前用户的密码
     */
    @RequestMapping("/changePwd")
    @ResponseBody
    public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
        if (!newPwd.equals(rePwd)) {
            throw new ServiceException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
        }
        if (!passwordService.isAcceptableNewPassword(newPwd)) {
            throw new ServiceException(BizExceptionEnum.PASSWORD_TOO_WEAK);
        }
        Integer userId = ShiroKit.getUser().getId();
        User user = userService.selectById(userId);
        if (passwordService.matches(oldPwd, user.getPassword(), user.getSalt())) {
            user.setPassword(passwordService.encode(newPwd));
            user.setSalt("");
            user.updateById();
            return SUCCESS_TIP;
        } else {
            throw new ServiceException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }

    /**
     * 查询管理员列表
     */
    @RequestMapping("/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) Integer deptid) {
        if (ShiroKit.isAdmin()) {
            List<Map<String, Object>> users = userService.selectUsers(null, name, beginTime, endTime, deptid);
            return new UserWarpper(users).wrap();
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            List<Map<String, Object>> users = userService.selectUsers(dataScope, name, beginTime, endTime, deptid);
            return new UserWarpper(users).wrap();
        }
    }

    /**
     * 添加管理员
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加管理员", key = "account", dict = UserDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData add(@Valid UserDto user, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (!passwordService.isAcceptableNewPassword(user.getPassword())) {
            throw new ServiceException(BizExceptionEnum.PASSWORD_TOO_WEAK);
        }

        // 判断账号是否重复
        User theUser = userService.getByAccount(user.getAccount());
        if (theUser != null) {
            throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
        }

        // 完善账号信息
        user.setSalt("");
        user.setPassword(passwordService.encode(user.getPassword()));
        user.setStatus(ManagerStatus.OK.getCode());
        user.setCreatetime(new Date());

        this.userService.insert(UserFactory.createUser(user));
        return SUCCESS_TIP;
    }

    /**
     * 修改管理员
     *
     * @throws NoPermissionException
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "修改管理员", key = "account", dict = UserDict.class)
    @ResponseBody
    public ResponseData edit(@Valid UserDto user, BindingResult result) throws NoPermissionException {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        User oldUser = userService.selectById(user.getId());

        if (ShiroKit.hasRole(Const.ADMIN_NAME)) {
            this.userService.updateById(UserFactory.editUser(user, oldUser));
            return SUCCESS_TIP;
        } else {
            assertAuth(user.getId());
            ShiroUser shiroUser = ShiroKit.getUser();
            if (shiroUser.getId().equals(user.getId())) {
                this.userService.updateById(UserFactory.editOwnProfile(user, oldUser));
                return SUCCESS_TIP;
            } else {
                throw new ServiceException(BizExceptionEnum.NO_PERMITION);
            }
        }
    }

    /**
     * 删除管理员（逻辑删除）
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除管理员", key = "userId", dict = UserDict.class)
    @Permission
    @ResponseBody
    public ResponseData delete(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.DELETED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 查看管理员详情
     */
    @RequestMapping("/view/{userId}")
    @ResponseBody
    public User view(@PathVariable Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        return this.userService.selectById(userId);
    }

    /**
     * 重置管理员的密码
     */
    @RequestMapping("/reset")
    @BussinessLog(value = "重置管理员密码", key = "userId", dict = UserDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData reset(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        String temporaryPassword = passwordService.generateTemporaryPassword();
        user.setSalt("");
        user.setPassword(passwordService.encode(temporaryPassword));
        this.userService.updateById(user);
        return ResponseData.success(Map.of("temporaryPassword", temporaryPassword));
    }

    /**
     * 冻结用户
     */
    @RequestMapping("/freeze")
    @BussinessLog(value = "冻结用户", key = "userId", dict = UserDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData freeze(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能冻结超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_FREEZE_ADMIN);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.FREEZED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 解除冻结用户
     */
    @RequestMapping("/unfreeze")
    @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData unfreeze(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.OK.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 分配角色
     */
    @RequestMapping("/setRole")
    @BussinessLog(value = "分配角色", key = "userId,roleIds", dict = UserDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData setRole(@RequestParam("userId") Integer userId, @RequestParam("roleIds") String roleIds) {
        if (ToolUtil.isOneEmpty(userId, roleIds)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能修改超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        assertAuth(userId);
        this.userService.setRoles(userId, roleIds);
        return SUCCESS_TIP;
    }

    /**
     * 上传图片
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture,
                         HttpServletRequest request) {
        ShiroUser currentUser = ShiroKit.getUser();
        if (currentUser == null
                || !uploadRateLimitService.tryAcquire(currentUser.getId(), request.getRemoteAddr())) {
            throw new UploadRateLimitException();
        }
        String originalFilename = picture.getOriginalFilename();
        String suffix = originalFilename == null
                ? "" : ToolUtil.getFileSuffix(originalFilename).toLowerCase(Locale.ROOT);
        if (picture.isEmpty() || picture.getSize() > MAX_IMAGE_BYTES || !IMAGE_EXTENSIONS.contains(suffix)) {
            throw new InvalidUploadException();
        }
        String outputFormat = "jpeg".equals(suffix) ? "jpg" : suffix;
        String pictureName = UUID.randomUUID().toString() + "." + outputFormat;
        Path target = null;
        try {
            BufferedImage image = readValidatedImage(picture);
            Path uploadRoot = new File(gunsProperties.getFileUploadPath()).toPath()
                    .toAbsolutePath().normalize();
            Files.createDirectories(uploadRoot);
            target = uploadRoot.resolve(pictureName).normalize();
            if (!target.startsWith(uploadRoot)) {
                throw new IOException("Invalid upload path");
            }
            try (OutputStream fileOutput = Files.newOutputStream(
                    target, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
                 OutputStream limitedOutput = new LimitedOutputStream(fileOutput, MAX_IMAGE_BYTES)) {
                if (!ImageIO.write(image, outputFormat, limitedOutput)) {
                    throw new IOException("Unsupported image format");
                }
            }
        } catch (IOException e) {
            if (target != null) {
                try {
                    Files.deleteIfExists(target);
                } catch (IOException cleanupError) {
                    e.addSuppressed(cleanupError);
                }
            }
            throw new InvalidUploadException();
        }
        return pictureName;
    }

    /**
     * 先从图片头读取尺寸，确认像素上限后才分配完整像素缓冲区。
     */
    private BufferedImage readValidatedImage(MultipartFile picture) throws IOException {
        try (InputStream input = picture.getInputStream();
             ImageInputStream imageInput = ImageIO.createImageInputStream(input)) {
            if (imageInput == null) {
                throw new IOException("Invalid image stream");
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);
            if (!readers.hasNext()) {
                throw new IOException("Unsupported image format");
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInput, true, true);
                validateImageDimensions(reader.getWidth(0), reader.getHeight(0));
                BufferedImage image = reader.read(0);
                if (image == null) {
                    throw new IOException("Invalid image");
                }
                return image;
            } finally {
                reader.dispose();
            }
        }
    }

    static void validateImageDimensions(int width, int height) throws IOException {
        if (width <= 0 || height <= 0 || (long) width * height > MAX_IMAGE_PIXELS) {
            throw new IOException("Invalid image dimensions");
        }
    }

    static final class LimitedOutputStream extends FilterOutputStream {

        private long remaining;

        LimitedOutputStream(OutputStream output, long maximumBytes) {
            super(output);
            this.remaining = maximumBytes;
        }

        @Override
        public void write(int value) throws IOException {
            ensureCapacity(1);
            out.write(value);
            remaining--;
        }

        @Override
        public void write(byte[] bytes, int offset, int length) throws IOException {
            ensureCapacity(length);
            out.write(bytes, offset, length);
            remaining -= length;
        }

        private void ensureCapacity(int requestedBytes) throws IOException {
            if (requestedBytes < 0 || requestedBytes > remaining) {
                throw new IOException("Encoded image exceeds size limit");
            }
        }
    }

    /**
     * 判断当前登录的用户是否有操作这个用户的权限
     */
    private void assertAuth(Integer userId) {
        if (ShiroKit.isAdmin()) {
            return;
        }
        List<Integer> deptDataScope = ShiroKit.getDeptDataScope();
        User user = this.userService.selectById(userId);
        Integer deptid = user.getDeptid();
        if (deptDataScope.contains(deptid)) {
            return;
        } else {
            throw new ServiceException(BizExceptionEnum.NO_PERMITION);
        }

    }
}
