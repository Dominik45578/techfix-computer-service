package pk.wieik.ti.config;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletContext;
import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.security.AccessTokenVerifier;
import pk.wieik.ti.security.AuthenticatedFilter;
import pk.wieik.ti.security.AuthenticationFilter;
import pk.wieik.ti.security.RoleFilter;

final class SecurityFilters {
    private SecurityFilters() {}

    static void register(ServletContext servletContext, AccessTokenVerifier tokens) {
        add(servletContext, "authentication", new AuthenticationFilter(tokens), "/api/*");
        add(servletContext, "requireUser", new AuthenticatedFilter(), "/api/me/*");
        add(servletContext, "requireAdmin", new RoleFilter(UserRole.ADMIN), "/api/admin/*");
    }

    private static void add(ServletContext servletContext, String name, Filter filter, String urlPattern) {
        servletContext.addFilter(name, filter).addMappingForUrlPatterns(null, true, urlPattern);
    }
}
