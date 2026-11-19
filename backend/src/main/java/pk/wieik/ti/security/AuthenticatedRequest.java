package pk.wieik.ti.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import pk.wieik.ti.service.AuthenticatedUser;

import java.security.Principal;

final class AuthenticatedRequest extends HttpServletRequestWrapper {
    private static final String AUTH_TYPE = "Bearer";

    private final AuthenticatedUser user;

    AuthenticatedRequest(HttpServletRequest request, AuthenticatedUser user) {
        super(request);
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public String getRemoteUser() {
        return user.getName();
    }

    @Override
    public boolean isUserInRole(String role) {
        return user.role().name().equals(role);
    }

    @Override
    public String getAuthType() {
        return AUTH_TYPE;
    }
}
