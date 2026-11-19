package pk.wieik.ti.service;

import pk.wieik.ti.model.User;
import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.repository.DataAccessException;
import pk.wieik.ti.repository.UserRepository;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static pk.wieik.ti.service.UserValidation.*;

public class AuthService {
    private static final String UNKNOWN_USER_HASH = PasswordUtil.hashPassword(UUID.randomUUID().toString());

    private final UserRepository users;

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public Optional<AuthenticatedUser> authenticate(String email, String password) {
        if (email == null || email.isBlank() || email.trim().length() > MAX_EMAIL_LENGTH
                || password == null || password.length() > MAX_PASSWORD_LENGTH) {
            return Optional.empty();
        }
        Optional<User> user = users.findByEmail(email.trim().toLowerCase(Locale.ROOT));
        if (user.isEmpty()) {
            // Ten sam koszt PBKDF2 co dla istniejącego konta: czas odpowiedzi nie zdradza, czy e-mail jest w bazie.
            PasswordUtil.verifyPassword(password, UNKNOWN_USER_HASH);
            return Optional.empty();
        }
        return user
                .filter(found -> PasswordUtil.verifyPassword(password, found.passwordHash()))
                .map(AuthenticatedUser::from);
    }

    public Optional<AuthenticatedUser> findUser(long userId) {
        return users.findById(userId).map(AuthenticatedUser::from);
    }

    public AuthenticatedUser registerClient(RegistrationRequest request) {
        return AuthenticatedUser.from(register(request, UserRole.CLIENT));
    }

    public boolean createFirstAdmin(String email, String password) {
        if (users.count() > 0) {
            return false;
        }
        register(new RegistrationRequest(email, password, "Administrator", "Systemu", null, null),
                UserRole.ADMIN);
        return true;
    }

    /**
     * @throws UserNotFoundException    gdy konto zostało usunięte
     * @throws IllegalArgumentException gdy obecne hasło się nie zgadza albo nowe nie spełnia reguł
     */
    public AuthenticatedUser changePassword(long userId, String currentPassword, String newPassword) {
        User user = users.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!PasswordUtil.verifyPassword(currentPassword, user.passwordHash())) {
            throw new IllegalArgumentException("Obecne hasło jest nieprawidłowe");
        }
        validatePassword(newPassword);
        if (newPassword.equals(currentPassword)) {
            throw new IllegalArgumentException("Nowe hasło musi różnić się od obecnego");
        }
        if (!users.updatePassword(userId, PasswordUtil.hashPassword(newPassword))) {
            throw new UserNotFoundException();
        }
        return AuthenticatedUser.from(user);
    }

    // TODO: reset hasła (requestPasswordReset / resetPassword) - wymaga wysyłki maili i tabeli tokenów resetu.

    private User register(RegistrationRequest request, UserRole role) {
        String email = normalizeEmail(request.email());
        validatePassword(request.password());
        String firstName = normalizeName(request.firstName(), "Imię");
        String lastName = normalizeName(request.lastName(), "Nazwisko");
        String phoneNumber = normalizePhone(request.phoneNumber());
        validateDateOfBirth(request.dateOfBirth());

        if (users.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException();
        }
        User user = User.of(email, PasswordUtil.hashPassword(request.password()),
                firstName, lastName, phoneNumber, request.dateOfBirth(), role);
        try {
            return users.insert(user);
        } catch (DataAccessException ex) {
            if (users.existsByEmail(email)) {
                throw new EmailAlreadyRegisteredException(ex);
            }
            throw ex;
        }
    }
}
