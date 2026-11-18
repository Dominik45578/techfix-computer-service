package pk.wieik.ti.auth;

import pk.wieik.ti.domain.model.user.User;
import pk.wieik.ti.domain.model.user.UserRole;

public record AuthenticatedUser(long id, String email, String fullName, UserRole role) {
    static AuthenticatedUser from(User user) {
        return new AuthenticatedUser(user.id(), user.email(), user.fullName(), user.role());
    }
}
