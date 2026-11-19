package pk.wieik.ti.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pk.wieik.ti.controller.ErrorPage;

import java.io.IOException;

abstract class AuthorizationFilter extends HttpFilter {

    protected abstract boolean isAllowed(HttpServletRequest request);

    @Override
    protected final void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getUserPrincipal() == null) {
            rejectUnauthenticated(request, response);
            return;
        }
        if (!isAllowed(request)) {
            ErrorPage.forward(request, response, HttpServletResponse.SC_FORBIDDEN, "Brak uprawnień");
            return;
        }
        chain.doFilter(request, response);
    }

    private static void rejectUnauthenticated(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        boolean tokenRejected = BearerToken.from(request).isPresent();
        response.setHeader("WWW-Authenticate", tokenRejected ? "Bearer error=\"invalid_token\"" : "Bearer");
        ErrorPage.forward(request, response, HttpServletResponse.SC_UNAUTHORIZED,
                tokenRejected ? "Token jest nieprawidłowy albo wygasł" : "Wymagane uwierzytelnienie");
    }
}
