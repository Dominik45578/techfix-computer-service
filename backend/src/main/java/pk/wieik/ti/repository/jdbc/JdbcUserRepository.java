package pk.wieik.ti.repository.jdbc;

import pk.wieik.ti.model.User;
import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static pk.wieik.ti.repository.jdbc.SqlTypes.*;

final class JdbcUserRepository extends JdbcRepository<User> implements UserRepository {

    JdbcUserRepository(Jdbc jdbc) {
        super(jdbc, "users", JdbcUserRepository::map);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findOneWhere("email = ? COLLATE NOCASE", email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return countWhere("email = ? COLLATE NOCASE", email) > 0;
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return findWhere("role = ? ORDER BY last_name, first_name", toDb(role));
    }

    @Override
    public User insert(User user) {
        long id = jdbc.insert("""
                INSERT INTO users (email, password_hash, first_name, last_name,
                                   phone_number, date_of_birth, role, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                user.email(), user.passwordHash(), user.firstName(), user.lastName(),
                user.phoneNumber(), toDb(user.dateOfBirth()), toDb(user.role()),
                toDb(user.createdAt()));
        return user.withId(id);
    }

    /** Aktualizuje dane profilu i rolę. Hasło zmienia się osobno przez {@link #updatePassword}. */
    @Override
    public boolean update(User user) {
        return jdbc.update("""
                UPDATE users
                SET email = ?, first_name = ?, last_name = ?,
                    phone_number = ?, date_of_birth = ?, role = ?
                WHERE id = ?
                """,
                user.email(), user.firstName(), user.lastName(),
                user.phoneNumber(), toDb(user.dateOfBirth()), toDb(user.role()),
                user.id()) == 1;
    }

    @Override
    public boolean updatePassword(long userId, String passwordHash) {
        return jdbc.update("UPDATE users SET password_hash = ? WHERE id = ?",
                passwordHash, userId) == 1;
    }

    private static User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone_number"),
                getDate(rs, "date_of_birth"),
                getEnum(rs, "role", UserRole.class),
                getDateTime(rs, "created_at"));
    }
}
