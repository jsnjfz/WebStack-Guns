package com.jsnjfz.manage.core.common.constant.factory;

import com.baomidou.mybatisplus.plugins.Page;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageFactoryTest {

    private final PageFactory<Object> pageFactory = new PageFactory<>();

    @Test
    void acceptsOnlyExplicitlyAllowedSortFields() {
        MockHttpServletRequest request = request("25", "50");
        request.setParameter("sort", "createtime");
        request.setParameter("order", "asc");

        Page<Object> page = pageFactory.createPage(request, "logname", "createtime");

        assertTrue(page.isOpenSort());
        assertEquals("createtime", page.getOrderByField());
        assertTrue(page.isAsc());
        assertEquals(3, page.getCurrent());
        assertEquals(25, page.getLimit());
    }

    @Test
    void disablesSortingForUnknownFields() {
        MockHttpServletRequest request = request("20", "0");
        request.setParameter("sort", "unknown_field");
        request.setParameter("order", "asc");

        Page<Object> page = pageFactory.createPage(request, "createtime");

        assertFalse(page.isOpenSort());
        assertNull(page.getOrderByField());
    }

    @Test
    void boundsAndDefaultsPaginationInput() {
        MockHttpServletRequest boundedRequest = request("999999", "-10");
        Page<Object> bounded = pageFactory.createPage(boundedRequest);
        assertEquals(PageFactory.MAX_LIMIT, bounded.getLimit());
        assertEquals(1, bounded.getCurrent());

        MockHttpServletRequest malformedRequest = request("not-a-number", "not-a-number");
        Page<Object> defaults = pageFactory.createPage(malformedRequest);
        assertEquals(PageFactory.DEFAULT_LIMIT, defaults.getLimit());
        assertEquals(1, defaults.getCurrent());
    }

    private MockHttpServletRequest request(String limit, String offset) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", limit);
        request.setParameter("offset", offset);
        return request;
    }
}
