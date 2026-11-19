package pk.wieik.ti.controller.dto;

import pk.wieik.ti.model.User;
import pk.wieik.ti.model.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfileResponse(long id, String email, String firstName, String lastName,
                              String phoneNumber, LocalDate dateOfBirth, UserRole role, LocalDateTime createdAt) {
    public static ProfileResponse from(User user) {
        return new ProfileResponse(user.id(), user.email(), user.firstName(), user.lastName(),
                user.phoneNumber(), user.dateOfBirth(), user.role(), user.createdAt());
    }
}
