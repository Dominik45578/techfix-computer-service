package pk.wieik.ti.service;

import pk.wieik.ti.model.User;
import pk.wieik.ti.model.UserRole;

public record AuthenticatedUser(long id, String email, String fullName, UserRole role) {
    static AuthenticatedUser from(User user) {
        return new AuthenticatedUser(user.id(), user.email(), user.fullName(), user.role());
    }
}
