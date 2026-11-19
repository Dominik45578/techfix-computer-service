package pk.wieik.ti.security;

import jakarta.servlet.http.HttpServletRequest;
import pk.wieik.ti.model.UserRole;

public final class RoleFilter extends AuthorizationFilter {
    private final UserRole requiredRole;

    public RoleFilter(UserRole requiredRole) {
        this.requiredRole = requiredRole;
    }

    @Override
    protected boolean isAllowed(HttpServletRequest request) {
        return request.isUserInRole(requiredRole.name());
    }
}
