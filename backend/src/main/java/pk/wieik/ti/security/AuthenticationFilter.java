package pk.wieik.ti.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthenticationFilter extends HttpFilter {
    private final AccessTokenVerifier tokens;

    public AuthenticationFilter(AccessTokenVerifier tokens) {
        this.tokens = tokens;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest authenticated = BearerToken.from(request)
                .flatMap(tokens::verify)
                .<HttpServletRequest>map(user -> new AuthenticatedRequest(request, user))
                .orElse(request);
        chain.doFilter(authenticated, response);
    }
}
