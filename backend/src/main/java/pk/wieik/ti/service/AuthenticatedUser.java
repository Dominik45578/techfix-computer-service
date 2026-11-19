package pk.wieik.ti.service;

import pk.wieik.ti.model.User;
import pk.wieik.ti.model.UserRole;

import java.security.Principal;

public record AuthenticatedUser(long id, String email, String fullName, UserRole role) implements Principal {
    static AuthenticatedUser from(User user) {
        return new AuthenticatedUser(user.id(), user.email(), user.fullName(), user.role());
    }

    @Override
    public String getName() {
        return email;
    }
}
