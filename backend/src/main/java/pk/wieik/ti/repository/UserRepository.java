package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository extends JdbcRepository<User> {
    public UserRepository(Jdbc jdbc) {
        super(jdbc, "uzytkownicy", UserRepository::map);
    }

    public Optional<User> findByLogin(String login) throws SQLException {
        return findOneWhere("login = ?", login);
    }

    public User insert(User user) throws SQLException {
        long id = jdbc.insert(
                "INSERT INTO uzytkownicy (login, haslo, uprawnienia) VALUES (?, ?, ?)",
                user.login(), user.hasloHash(), user.uprawnienia());
        return new User(id, user.login(), user.hasloHash(), user.uprawnienia());
    }

    public boolean update(User user) throws SQLException {
        return jdbc.update(
                "UPDATE uzytkownicy SET login = ?, haslo = ?, uprawnienia = ? WHERE id = ?",
                user.login(), user.hasloHash(), user.uprawnienia(), user.id()) == 1;
    }

    private static User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("login"),
                rs.getString("haslo"),
                rs.getInt("uprawnienia"));
    }
}
