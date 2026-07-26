package com.jsnjfz.manage.core.security;

import com.jsnjfz.manage.config.properties.GunsProperties;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * 基于 Session 的 CSRF 防护，兼容现有 jQuery/WebUploader 页面。
 */
public class CsrfFilter extends OncePerRequestFilter {

    public static final String COOKIE_NAME = "XSRF-TOKEN";
    public static final String HEADER_NAME = "X-CSRF-TOKEN";
    public static final String REQUEST_ATTRIBUTE = "csrfToken";
    private static final String SESSION_ATTRIBUTE = CsrfFilter.class.getName() + ".TOKEN";
    private static final Pattern LEGACY_GET_MUTATION = Pattern.compile(
            ".*/(add|update|delete|edit|remove|reset|freeze|unfreeze|setRole|setAuthority|changePwd|upload|delLog|delLoginLog)$");

    private final SecureRandom secureRandom = new SecureRandom();
    private final GunsProperties gunsProperties;

    public CsrfFilter(GunsProperties gunsProperties) {
        this.gunsProperties = gunsProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (!contextPath.isEmpty() && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        return uri.startsWith("/static/")
                || uri.equals("/favicon.ico")
                || uri.startsWith("/gunsApi/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String token = session == null ? null : (String) session.getAttribute(SESSION_ATTRIBUTE);

        if (token == null && shouldCreateToken(request)) {
            session = request.getSession(true);
            token = createToken();
            session.setAttribute(SESSION_ATTRIBUTE, token);
        }

        if (token != null) {
            request.setAttribute(REQUEST_ATTRIBUTE, token);
            if (!token.equals(readCookie(request, COOKIE_NAME))) {
                addTokenCookie(request, response, token);
            }
        }

        if (requiresValidation(request) && !matches(token, submittedToken(request))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"message\":\"CSRF token invalid\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldCreateToken(HttpServletRequest request) {
        String method = request.getMethod();
        return "GET".equals(method) || "HEAD".equals(method);
    }

    private boolean requiresValidation(HttpServletRequest request) {
        String method = request.getMethod();
        if (!"GET".equals(method) && !"HEAD".equals(method) && !"OPTIONS".equals(method)) {
            return true;
        }
        return LEGACY_GET_MUTATION.matcher(request.getRequestURI()).matches();
    }

    private String submittedToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_NAME);
        return token == null || token.isEmpty() ? request.getParameter("_csrf") : token;
    }

    private boolean matches(String expected, String submitted) {
        if (expected == null || submitted == null) {
            return false;
        }
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.US_ASCII),
                submitted.getBytes(StandardCharsets.US_ASCII));
    }

    private String createToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void addTokenCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        String path = request.getContextPath().isEmpty() ? "/" : request.getContextPath();
        StringBuilder cookie = new StringBuilder(COOKIE_NAME)
                .append('=').append(token)
                .append("; Path=").append(path)
                .append("; SameSite=Lax");
        if (Boolean.TRUE.equals(gunsProperties.getSecureCookie())) {
            cookie.append("; Secure");
        }
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
