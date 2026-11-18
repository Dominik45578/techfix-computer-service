package pk.wieik.ti.service;

import pk.wieik.ti.model.User;
import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

public class AuthService {
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MIN_PASSWORD_LENGTH = 12;

    private final UserRepository users;

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public Optional<AuthenticatedUser> authenticate(String email, String password) {
        if (!isValidEmail(email) || password == null) {
            return Optional.empty();
        }
        return users.findByEmail(email.trim())
                .filter(user -> PasswordUtil.verifyPassword(password, user.passwordHash()))
                .map(AuthenticatedUser::from);
    }

    public User register(String email, String password, String firstName, String lastName,
                         String phoneNumber, LocalDate dateOfBirth, UserRole role) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException(
                    "E-mail musi mieć od 1 do " + MAX_EMAIL_LENGTH + " znaków");
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Hasło musi mieć co najmniej " + MIN_PASSWORD_LENGTH + " znaków");
        }
        if (users.existsByEmail(email.trim())) {
            throw new IllegalArgumentException("Konto z takim adresem e-mail już istnieje");
        }
        return users.insert(User.of(email.trim(), PasswordUtil.hashPassword(password),
                firstName, lastName, phoneNumber, dateOfBirth, role));
    }

    public boolean createFirstAdmin(String email, String password) {
        if (users.count() > 0) {
            return false;
        }
        register(email, password, "Administrator", "Systemu", null, null, UserRole.ADMIN);
        return true;
    }

    private static boolean isValidEmail(String email) {
        return email != null && !email.isBlank() && email.trim().length() <= MAX_EMAIL_LENGTH;
    }
}
