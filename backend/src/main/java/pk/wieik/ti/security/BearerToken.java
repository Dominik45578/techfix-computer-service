package pk.wieik.ti.security;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

final class BearerToken {
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private BearerToken() {}

    static Optional<String> from(HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        if (header == null || !header.regionMatches(true, 0, PREFIX, 0, PREFIX.length())) {
            return Optional.empty();
        }
        String token = header.substring(PREFIX.length()).trim();
        return token.isEmpty() ? Optional.empty() : Optional.of(token);
    }
}
