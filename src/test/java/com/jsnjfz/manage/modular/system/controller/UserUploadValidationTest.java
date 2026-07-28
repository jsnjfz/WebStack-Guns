package com.jsnjfz.manage.modular.system.controller;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserUploadValidationTest {

    @Test
    void acceptsImagesWithinPixelLimit() {
        assertDoesNotThrow(() -> UserMgrController.validateImageDimensions(4000, 4000));
    }

    @Test
    void rejectsInvalidOrOversizedImageDimensionsBeforeDecode() {
        assertThrows(IOException.class,
                () -> UserMgrController.validateImageDimensions(0, 100));
        assertThrows(IOException.class,
                () -> UserMgrController.validateImageDimensions(4097, 4097));
        assertThrows(IOException.class,
                () -> UserMgrController.validateImageDimensions(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    void stopsEncodedOutputAtConfiguredLimit() throws IOException {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        UserMgrController.LimitedOutputStream output =
                new UserMgrController.LimitedOutputStream(target, 4);

        output.write(new byte[]{1, 2, 3, 4});

        assertEquals(4, target.size());
        assertThrows(IOException.class, () -> output.write(5));
    }
}
