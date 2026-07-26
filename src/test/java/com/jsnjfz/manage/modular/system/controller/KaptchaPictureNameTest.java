package com.jsnjfz.manage.modular.system.controller;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 校验 {@link KaptchaController} 的图片文件名白名单：
 * 既要放行历史遗留的 32 位十六进制命名（站点表中占绝大多数），
 * 也要继续挡住路径穿越与非图片扩展名。
 */
class KaptchaPictureNameTest {

    private static Pattern pictureName() throws Exception {
        Field field = KaptchaController.class.getDeclaredField("PICTURE_NAME");
        field.setAccessible(true);
        return (Pattern) field.get(null);
    }

    private static boolean matches(String name) throws Exception {
        return pictureName().matcher(name).matches();
    }

    @Test
    void acceptsLegacyMd5StyleNames() throws Exception {
        // 站点表中的历史数据格式，此前被误拒导致图片全部 404
        assertTrue(matches("57ab570b631cc25a23390f507d496bc6.png"));
        assertTrue(matches("7db1257f40b9b04482744387a00b359d.PNG".toLowerCase()));
    }

    @Test
    void acceptsUuidStyleNamesFromUpload() throws Exception {
        // /user/upload 生成的新文件名
        assertTrue(matches("8681fb7d-6ee4-4e01-9a61-105b97247704.jpg"));
        assertTrue(matches("8681fb7d-6ee4-4e01-9a61-105b97247704.jpeg"));
        assertTrue(matches("8681fb7d-6ee4-4e01-9a61-105b97247704.gif"));
    }

    @Test
    void rejectsPathTraversalAndSeparators() throws Exception {
        assertFalse(matches("../../etc/passwd"));
        assertFalse(matches("..%2f..%2fetc%2fpasswd"));
        assertFalse(matches("57ab570b631cc25a23390f507d496bc6/../secret.png"));
        assertFalse(matches("/etc/passwd"));
        assertFalse(matches("..\\windows\\win.ini"));
    }

    @Test
    void rejectsNonImageAndMalformedNames() throws Exception {
        assertFalse(matches("57ab570b631cc25a23390f507d496bc6.jsp"));
        assertFalse(matches("57ab570b631cc25a23390f507d496bc6.png.jsp"));
        assertFalse(matches("application.yml"));
        // 长度不足 32 位、含非十六进制字符
        assertFalse(matches("57ab570b631cc25a23390f507d496bc.png"));
        assertFalse(matches("zzab570b631cc25a23390f507d496bc6.png"));
        assertFalse(matches(""));
    }
}
