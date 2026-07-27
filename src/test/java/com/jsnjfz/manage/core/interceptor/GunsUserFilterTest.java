package com.jsnjfz.manage.core.interceptor;

import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GunsUserFilterTest {

    @Test
    void allowsOnlyAuthenticatedSubjects() {
        Subject subject = mock(Subject.class);
        TestableGunsUserFilter filter = new TestableGunsUserFilter(subject);

        when(subject.isAuthenticated()).thenReturn(false);
        assertFalse(filter.isAllowed());

        when(subject.isAuthenticated()).thenReturn(true);
        assertTrue(filter.isAllowed());
    }

    private static class TestableGunsUserFilter extends GunsUserFilter {

        private final Subject subject;

        TestableGunsUserFilter(Subject subject) {
            this.subject = subject;
        }

        boolean isAllowed() {
            return isAccessAllowed(
                    new MockHttpServletRequest(),
                    new MockHttpServletResponse(),
                    null);
        }

        @Override
        protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
            return false;
        }

        @Override
        protected Subject getSubject(ServletRequest request, ServletResponse response) {
            return subject;
        }
    }
}
