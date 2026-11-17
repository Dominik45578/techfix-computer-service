package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.User;
import pk.wieik.ti.model.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static pk.wieik.ti.db.SqlTypes.*;

public class UserRepository extends JdbcRepository<User> {
    public UserRepository(Jdbc jdbc) {
        super(jdbc, "users", UserRepository::map);
    }

    public Optional<User> findByEmail(String email) throws SQLException {
        return findOneWhere("email = ? COLLATE NOCASE", email);
    }

    public boolean existsByEmail(String email) throws SQLException {
        return jdbc.queryLong("SELECT COUNT(*) FROM users WHERE email = ? COLLATE NOCASE", email) > 0;
    }

    public List<User> findByRole(UserRole role) throws SQLException {
        return jdbc.query("SELECT * FROM users WHERE role = ? ORDER BY last_name, first_name",
                UserRepository::map, toDb(role));
    }

    public User insert(User user) throws SQLException {
        long id = jdbc.insert("""
                INSERT INTO users (email, password_hash, first_name, last_name,
                                   phone_number, date_of_birth, role, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                user.email(), user.passwordHash(), user.firstName(), user.lastName(),
                user.phoneNumber(), toDb(user.dateOfBirth()), toDb(user.role()),
                toDb(user.createdAt()));
        return new User(id, user.email(), user.passwordHash(), user.firstName(), user.lastName(),
                user.phoneNumber(), user.dateOfBirth(), user.role(), user.createdAt());
    }

    /** Aktualizuje dane profilu i rolę. Hasło zmienia się osobno przez {@link #updatePassword}. */
    public boolean update(User user) throws SQLException {
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

    public boolean updatePassword(long userId, String passwordHash) throws SQLException {
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
