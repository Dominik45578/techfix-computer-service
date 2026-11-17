package pk.wieik.ti.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record User(long id, String email, String passwordHash, String firstName, String lastName,
                   String phoneNumber, LocalDate dateOfBirth, UserRole role, LocalDateTime createdAt) {

    public User {
        Checks.notBlank(email, "email");
        Checks.notBlank(passwordHash, "password");
        Checks.notBlank(firstName, "firstName");
        Checks.notBlank(lastName, "lastName");
        Checks.notNull(role, "role");
        Checks.notNull(createdAt, "createdAt");
    }

    public static User of(String email, String passwordHash,
                          String firstName, String lastName,
                          String phoneNumber, LocalDate dateOfBirth,
                          UserRole role) {
        return new User(0, email, passwordHash, firstName, lastName,
                phoneNumber, dateOfBirth, role, LocalDateTime.now(ZoneOffset.UTC));
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    @Override
    public String toString() {
        return "User[id=" + id + ", email=" + email + ", role=" + role + "]";
    }
}
