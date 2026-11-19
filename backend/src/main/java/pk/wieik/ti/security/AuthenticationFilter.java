package pk.wieik.ti.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Ustala, kto wysłał żądanie (nie decyduje, czy wolno). Ważny token Bearer → żądanie opakowane
 * w {@link AuthenticatedRequest}; brak lub zły token → żądanie anonimowe. Blokuje dopiero {@link RoleFilter}.
 * Zły token nie daje tu 401, bo przeterminowany access token nie może blokować np. {@code /api/auth/refresh}.
 */
public class AuthenticationFilter extends HttpFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final AccessTokenVerifier tokens;

    public AuthenticationFilter(AccessTokenVerifier tokens) {
        this.tokens = tokens;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest authenticated = bearerToken(request)
                .flatMap(tokens::verify)
                .<HttpServletRequest>map(user -> new AuthenticatedRequest(request, user))
                .orElse(request);
        chain.doFilter(authenticated, response);
    }

    static Optional<String> bearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            return Optional.empty();
        }
        String token = header.substring(BEARER_PREFIX.length()).trim();
        return token.isEmpty() ? Optional.empty() : Optional.of(token);
    }
}
