package pk.wieik.ti.auth;

import pk.wieik.ti.model.User;
import pk.wieik.ti.repository.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    private static final int MAX_LOGIN_LENGTH = 64;
    private static final int MIN_PASSWORD_LENGTH = 12;

    private final UserRepository users;

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public Optional<AuthenticatedUser> authenticate(String login, String password) throws SQLException {
        if (!isValidLogin(login) || password == null) {
            return Optional.empty();
        }
        return users.findByLogin(login.trim())
                .filter(user -> PasswordUtil.verifyPassword(password, user.hasloHash()))
                .map(AuthenticatedUser::from);
    }

    public User register(String login, String password, int uprawnienia) throws SQLException {
        if (!isValidLogin(login)) {
            throw new IllegalArgumentException(
                    "Login musi mieć od 1 do " + MAX_LOGIN_LENGTH + " znaków");
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Hasło musi mieć co najmniej " + MIN_PASSWORD_LENGTH + " znaków");
        }
        return users.insert(User.of(login.trim(), PasswordUtil.hashPassword(password), uprawnienia));
    }

    public boolean createFirstAdmin(String login, String password) throws SQLException {
        if (users.count() > 0) {
            return false;
        }
        register(login, password, User.ADMINISTRATOR);
        return true;
    }

    private static boolean isValidLogin(String login) {
        return login != null && !login.isBlank() && login.trim().length() <= MAX_LOGIN_LENGTH;
    }
}
