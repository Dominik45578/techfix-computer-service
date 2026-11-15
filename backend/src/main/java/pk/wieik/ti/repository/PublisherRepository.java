package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.Publisher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PublisherRepository extends JdbcRepository<Publisher> {
    public PublisherRepository(Jdbc jdbc) {
        super(jdbc, "wydawcy", PublisherRepository::map);
    }

    public Optional<Publisher> findByNazwa(String nazwa) throws SQLException {
        return findOneWhere("nazwa = ?", nazwa);
    }

    public Publisher insert(Publisher publisher) throws SQLException {
        long id = jdbc.insert("INSERT INTO wydawcy (nazwa) VALUES (?)", publisher.nazwa());
        return new Publisher(id, publisher.nazwa());
    }

    public boolean update(Publisher publisher) throws SQLException {
        return jdbc.update(
                "UPDATE wydawcy SET nazwa = ? WHERE id = ?",
                publisher.nazwa(), publisher.id()) == 1;
    }

    private static Publisher map(ResultSet rs) throws SQLException {
        return new Publisher(
                rs.getLong("id"),
                rs.getString("nazwa"));
    }
}
