package pk.wieik.ti.security;

import jakarta.servlet.http.HttpServletRequest;

public final class AuthenticatedFilter extends AuthorizationFilter {

    @Override
    protected boolean isAllowed(HttpServletRequest request) {
        return true;
    }
}
