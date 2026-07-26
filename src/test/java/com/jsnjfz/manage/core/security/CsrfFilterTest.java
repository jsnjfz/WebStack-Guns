package com.jsnjfz.manage.core.security;

import com.jsnjfz.manage.config.properties.GunsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
