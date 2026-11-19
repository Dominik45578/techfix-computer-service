package pk.wieik.ti.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pk.wieik.ti.security.token.IssuedToken;

import java.util.Arrays;
import java.util.Optional;

final class RefreshTokenCookie {
    private static final String NAME = "refresh_token";
    private static final String PATH = "/api/auth";
    private static final String SAME_SITE = "Strict";

    private RefreshTokenCookie() {}

    static Optional<String> read(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    static void write(HttpServletRequest request, HttpServletResponse response, IssuedToken token) {
        response.addCookie(build(request, token.value(), (int) token.lifetime().toSeconds()));
    }

    static void clear(HttpServletRequest request, HttpServletResponse response) {
        response.addCookie(build(request, "", 0));
    }

    private static Cookie build(HttpServletRequest request, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(NAME, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(isHttps(request));
        cookie.setPath(request.getContextPath() + PATH);
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setAttribute("SameSite", SAME_SITE);
        return cookie;
    }

    private static boolean isHttps(HttpServletRequest request) {
        return request.isSecure() || "https".equalsIgnoreCase(request.getHeader("X-Forwarded-Proto"));
    }
}
