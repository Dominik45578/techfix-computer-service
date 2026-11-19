package pk.wieik.ti.service;

import pk.wieik.ti.model.User;
import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.repository.DataAccessException;
import pk.wieik.ti.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class AuthService {
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MIN_PASSWORD_LENGTH = 12;
    private static final int MAX_PASSWORD_LENGTH = 128;
    private static final int MAX_NAME_LENGTH = 100;
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE = Pattern.compile("^\\+?[0-9 ()-]{6,20}$");
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

    private static String normalizeEmail(String email) {
        String value = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (value.isEmpty() || value.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("E-mail musi mieć od 1 do " + MAX_EMAIL_LENGTH + " znaków");
        }
        if (!EMAIL.matcher(value).matches()) {
            throw new IllegalArgumentException("Niepoprawny adres e-mail");
        }
        return value;
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Hasło musi mieć co najmniej " + MIN_PASSWORD_LENGTH + " znaków");
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Hasło może mieć najwyżej " + MAX_PASSWORD_LENGTH + " znaków");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("Hasło nie może składać się z samych spacji");
        }
    }

    private static String normalizeName(String name, String label) {
        String value = name == null ? "" : name.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(label + " nie może być puste");
        }
        if (value.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(label + " może mieć najwyżej " + MAX_NAME_LENGTH + " znaków");
        }
        return value;
    }

    /** Telefon jest opcjonalny: puste pole zapisujemy jako NULL. */
    private static String normalizePhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return null;
        }
        String value = phoneNumber.trim();
        if (!PHONE.matcher(value).matches()) {
            throw new IllegalArgumentException("Niepoprawny numer telefonu");
        }
        return value;
    }

    private static void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now(ZoneOffset.UTC))) {
            throw new IllegalArgumentException("Data urodzenia nie może być z przyszłości");
        }
    }
}
