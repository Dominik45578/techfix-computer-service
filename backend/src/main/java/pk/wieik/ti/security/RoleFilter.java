package pk.wieik.ti.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pk.wieik.ti.controller.ErrorServlet;
import pk.wieik.ti.model.UserRole;

import java.io.IOException;

/**
 * Wpuszcza tylko zalogowanych (opcjonalnie z konkretną rolą). Musi działać po {@link AuthenticationFilter};
 * kolejność i ścieżki ustala {@code AppListener}.
 */
public class RoleFilter extends HttpFilter {
    private final UserRole requiredRole;

    private RoleFilter(UserRole requiredRole) {
        this.requiredRole = requiredRole;
    }

    public static RoleFilter authenticated() {
        return new RoleFilter(null);
    }

    public static RoleFilter requireRole(UserRole role) {
        return new RoleFilter(role);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getUserPrincipal() == null) {
            rejectUnauthenticated(request, response);
            return;
        }
        if (requiredRole != null && !request.isUserInRole(requiredRole.name())) {
            ErrorServlet.send(request, response, HttpServletResponse.SC_FORBIDDEN, "Brak uprawnień");
            return;
        }
        chain.doFilter(request, response);
    }

    /** RFC 6750: 401 dla Bearer zawsze z {@code WWW-Authenticate}, a przy odrzuconym tokenie z {@code invalid_token}. */
    private static void rejectUnauthenticated(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (AuthenticationFilter.bearerToken(request).isPresent()) {
            response.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
            ErrorServlet.send(request, response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Token jest nieprawidłowy albo wygasł");
        } else {
            response.setHeader("WWW-Authenticate", "Bearer");
            ErrorServlet.send(request, response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Wymagane uwierzytelnienie");
        }
    }
}
