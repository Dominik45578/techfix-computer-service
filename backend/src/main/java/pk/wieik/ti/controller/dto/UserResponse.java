package pk.wieik.ti.controller.dto;

import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.service.AuthenticatedUser;

public record UserResponse(long id, String email, String fullName, UserRole role) {
    public static UserResponse from(AuthenticatedUser user) {
        return new UserResponse(user.id(), user.email(), user.fullName(), user.role());
    }
}
