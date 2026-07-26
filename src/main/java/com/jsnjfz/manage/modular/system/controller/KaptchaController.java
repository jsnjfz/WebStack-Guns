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

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.jsnjfz.manage.config.properties.GunsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 验证码生成
 *
 * @author fengshuonan
 * @date 2017-05-05 23:10
 */
@Controller
@RequestMapping("/kaptcha")
public class KaptchaController {

    /**
     * 允许的图片文件名格式，两种都要放行：
     * 1. UUID 形式（8-4-4-4-12）——当前 {@code /user/upload} 生成的新文件名；
     * 2. 32 位十六进制形式——历史遗留数据，站点表中占绝大多数，
     *    只认 UUID 会让这些图片全部 404。
     * 仍然只允许十六进制字符与白名单扩展名，不含 '/'、'\' 或 '.'，
     * 因此无法借文件名做路径穿越；下方仍保留 normalize + startsWith 兜底。
     */
    private static final Pattern PICTURE_NAME = Pattern.compile(
            "^(?:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
                    + "|[0-9a-fA-F]{32})\\.(?:jpg|jpeg|png|gif)$");

    @Autowired
    private GunsProperties gunsProperties;

    @Autowired
    private Producer producer;

    /**
     * 生成验证码
     */
    @RequestMapping("")
    public void index(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = producer.createText();

        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        // create the image with the text
        BufferedImage bi = producer.createImage(capText);
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write the data out
        try {
            ImageIO.write(bi, "jpg", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回图片
     *
     * @author stylefeng
     * @Date 2017/5/24 23:00
     */
    @RequestMapping("/{pictureId}")
    public void renderPicture(@PathVariable("pictureId") String pictureId, HttpServletResponse response) {
        if (pictureId == null || !PICTURE_NAME.matcher(pictureId).matches()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            Path root = new File(gunsProperties.getFileUploadPath()).toPath().toAbsolutePath().normalize();
            Path picture = root.resolve(pictureId).normalize();
            if (!picture.startsWith(root) || !Files.isRegularFile(picture)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String suffix = pictureId.substring(pictureId.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
            response.setContentType("jpg".equals(suffix) ? "image/jpeg" : "image/" + suffix);
            response.setHeader("X-Content-Type-Options", "nosniff");
            response.setHeader("Cache-Control", "private, max-age=86400");
            Files.copy(picture, response.getOutputStream());
        } catch (Exception e) {
            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
}
