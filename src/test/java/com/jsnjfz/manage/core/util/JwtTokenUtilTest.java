package com.jsnjfz.manage.core.util;

import com.jsnjfz.manage.config.properties.GunsProperties;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenUtilTest {

    @Test
    void signsAndValidatesWithConfiguredKey() {
        JwtTokenUtil tokenUtil = new JwtTokenUtil(propertiesWithKey((byte) 1));
        String token = tokenUtil.generateToken("1");

        tokenUtil.parseToken(token);
        assertEquals("1", tokenUtil.getUsernameFromToken(token));
        assertThrows(JwtException.class,
                () -> new JwtTokenUtil(propertiesWithKey((byte) 2)).parseToken(token));
    }

    @Test
    void rejectsShortConfiguredKey() {
        GunsProperties properties = new GunsProperties();
        properties.setJwtSecret(Base64.getEncoder().encodeToString(new byte[32]));

        assertThrows(IllegalArgumentException.class, () -> new JwtTokenUtil(properties));
    }

    private GunsProperties propertiesWithKey(byte fill) {
        byte[] key = new byte[64];
        java.util.Arrays.fill(key, fill);
        GunsProperties properties = new GunsProperties();
        properties.setJwtSecret(Base64.getEncoder().encodeToString(key));
        return properties;
    }
}
