package com.jsnjfz.manage.core.security;

import com.jsnjfz.manage.config.properties.GunsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsrfFilterTest {

    private final CsrfFilter filter = new CsrfFilter(new GunsProperties());

    @Test
    void issuesTokenAndRequiresItForPost() throws Exception {
        MockHttpServletRequest get = new MockHttpServletRequest("GET", "/login");
        MockHttpServletResponse getResponse = new MockHttpServletResponse();
        filter.doFilter(get, getResponse, new MockFilterChain());
        String token = (String) get.getAttribute(CsrfFilter.REQUEST_ATTRIBUTE);

        assertNotNull(token);
        assertNotNull(getResponse.getHeader("Set-Cookie"));

        MockHttpServletRequest rejected = new MockHttpServletRequest("POST", "/login");
        rejected.setSession(get.getSession());
        MockHttpServletResponse rejectedResponse = new MockHttpServletResponse();
        filter.doFilter(rejected, rejectedResponse, new MockFilterChain());
        assertEquals(403, rejectedResponse.getStatus());

        MockHttpServletRequest accepted = new MockHttpServletRequest("POST", "/login");
        accepted.setSession(get.getSession());
        accepted.setCookies(new Cookie(CsrfFilter.COOKIE_NAME, token));
        accepted.addHeader(CsrfFilter.HEADER_NAME, token);
        MockHttpServletResponse acceptedResponse = new MockHttpServletResponse();
        filter.doFilter(accepted, acceptedResponse, new MockFilterChain());
        assertEquals(200, acceptedResponse.getStatus());
    }

    @Test
    void adminAjaxAddsCsrfHeaderOnlyToSameOriginRequests() throws IOException {
        String script = classpathText("/static/js/common/ajax-object.js");
        String container = classpathText("/WEB-INF/view/common/_container.html");

        assertTrue(script.contains("ajaxSend.gunsCsrf"));
        assertTrue(script.contains("target.protocol === window.location.protocol"));
        assertTrue(script.contains("target.host === window.location.host"));
        assertTrue(script.contains("xhr.setRequestHeader(\"X-CSRF-TOKEN\", token)"));
        assertTrue(container.contains(
                "ajax-object.js?v=${env('app.asset-version','dev')}"));
    }

    private String classpathText(String path) throws IOException {
        try (InputStream input = getClass().getResourceAsStream(path)) {
            assertNotNull(input);
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
