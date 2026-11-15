package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AuthorRepository extends JdbcRepository<Author> {
    public AuthorRepository(Jdbc jdbc) {
        super(jdbc, "autorzy", AuthorRepository::map);
    }

    public Optional<Author> findByName(String imie, String nazwisko) throws SQLException {
        return findOneWhere("imie = ? AND nazwisko = ?", imie, nazwisko);
    }

    public Author insert(Author author) throws SQLException {
        long id = jdbc.insert(
                "INSERT INTO autorzy (imie, nazwisko) VALUES (?, ?)",
                author.imie(), author.nazwisko());
        return new Author(id, author.imie(), author.nazwisko());
    }

    public boolean update(Author author) throws SQLException {
        return jdbc.update(
                "UPDATE autorzy SET imie = ?, nazwisko = ? WHERE id = ?",
                author.imie(), author.nazwisko(), author.id()) == 1;
    }

    private static Author map(ResultSet rs) throws SQLException {
        return new Author(
                rs.getLong("id"),
                rs.getString("imie"),
                rs.getString("nazwisko"));
    }
}
