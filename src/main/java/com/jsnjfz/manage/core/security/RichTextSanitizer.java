package com.jsnjfz.manage.core.security;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

/**
 * 通知富文本白名单。保留编辑器常用格式，移除脚本、事件属性和危险 URL。
 */
@Component
public class RichTextSanitizer {

    private final PolicyFactory policy = Sanitizers.FORMATTING
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.STYLES)
            .and(Sanitizers.LINKS)
            .and(Sanitizers.TABLES)
            .and(Sanitizers.IMAGES);

    public String sanitize(String html) {
        return html == null ? "" : policy.sanitize(html);
    }
}
