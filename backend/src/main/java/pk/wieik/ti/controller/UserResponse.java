package pk.wieik.ti.controller;

import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.service.AuthenticatedUser;

/** Publiczne dane użytkownika w odpowiedziach API (bez hasha hasła). */
public record UserResponse(long id, String email, String fullName, UserRole role) {
    static UserResponse from(AuthenticatedUser user) {
        return new UserResponse(user.id(), user.email(), user.fullName(), user.role());
    }
}
