package pk.wieik.ti.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/** Tabela {@code users}. */
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

    /** Nowy, jeszcze niezapisany użytkownik (id = 0). */
    public static User of(String email, String passwordHash,
                          String firstName, String lastName,
                          String phoneNumber, LocalDate dateOfBirth,
                          UserRole role) {
        return new User(0, email, passwordHash, firstName, lastName,
                phoneNumber, dateOfBirth, role, LocalDateTime.now(ZoneOffset.UTC));
    }

    public User withId(long newId) {
        return new User(newId, email, passwordHash, firstName, lastName,
                phoneNumber, dateOfBirth, role, createdAt);
    }

    public User withProfile(String newFirstName, String newLastName,
                            String newPhoneNumber, LocalDate newDateOfBirth) {
        return new User(id, email, passwordHash, newFirstName, newLastName,
                newPhoneNumber, newDateOfBirth, role, createdAt);
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    /** Bez hasha hasła, żeby nie trafiał do logów. */
    @Override
    public String toString() {
        return "User[id=" + id + ", email=" + email + ", role=" + role + "]";
    }
}
