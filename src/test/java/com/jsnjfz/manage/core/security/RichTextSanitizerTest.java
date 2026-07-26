package com.jsnjfz.manage.core.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RichTextSanitizerTest {

    private final RichTextSanitizer sanitizer = new RichTextSanitizer();

    @Test
    void removesExecutableMarkupAndKeepsBasicFormatting() {
        String sanitized = sanitizer.sanitize(
                "<p><b>公告</b><img src=x onerror=alert(1)>"
                        + "<a href=\"javascript:alert(1)\">link</a><script>alert(1)</script></p>");

        assertTrue(sanitized.contains("<p>"));
        assertTrue(sanitized.contains("<b>公告</b>"));
        assertFalse(sanitized.toLowerCase().contains("script"));
        assertFalse(sanitized.toLowerCase().contains("onerror"));
        assertFalse(sanitized.toLowerCase().contains("javascript:"));
    }
}
