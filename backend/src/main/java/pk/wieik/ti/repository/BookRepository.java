package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.Book;
import pk.wieik.ti.model.BookDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BookRepository extends JdbcRepository<Book> {
    private static final String DETAILS_SELECT = """
            SELECT k.id, a.imie || ' ' || a.nazwisko AS autor,
                   k.tytul_pl, k.tytul_en, k.rok_wydania,
                   w.nazwa AS wydawca
            FROM ksiazki k
            JOIN autorzy a ON a.id = k.autor_id
            JOIN wydawcy w ON w.id = k.wydawca_id
            """;

    public BookRepository(Jdbc jdbc) {
        super(jdbc, "ksiazki", BookRepository::map);
    }

    public List<BookDetails> findAllDetails() throws SQLException {
        return jdbc.query(DETAILS_SELECT + "ORDER BY k.id LIMIT 200", BookRepository::mapDetails);
    }

    public Optional<BookDetails> findDetailsById(long id) throws SQLException {
        return jdbc.queryOne(DETAILS_SELECT + "WHERE k.id = ?", BookRepository::mapDetails, id);
    }

    public List<Book> findByAutorId(long autorId) throws SQLException {
        return jdbc.query("SELECT * FROM ksiazki WHERE autor_id = ? ORDER BY rok_wydania",
                BookRepository::map, autorId);
    }

    public Book insert(Book book) throws SQLException {
        long id = jdbc.insert("""
                INSERT INTO ksiazki (autor_id, tytul_pl, tytul_en, rok_wydania, wydawca_id)
                VALUES (?, ?, ?, ?, ?)
                """,
                book.autorId(), book.tytulPl(), book.tytulEn(), book.rokWydania(), book.wydawcaId());
        return new Book(id, book.autorId(), book.tytulPl(), book.tytulEn(),
                book.rokWydania(), book.wydawcaId());
    }

    public boolean update(Book book) throws SQLException {
        return jdbc.update("""
                UPDATE ksiazki
                SET autor_id = ?, tytul_pl = ?, tytul_en = ?, rok_wydania = ?, wydawca_id = ?
                WHERE id = ?
                """,
                book.autorId(), book.tytulPl(), book.tytulEn(), book.rokWydania(), book.wydawcaId(),
                book.id()) == 1;
    }

    private static Book map(ResultSet rs) throws SQLException {
        return new Book(
                rs.getLong("id"),
                rs.getLong("autor_id"),
                rs.getString("tytul_pl"),
                rs.getString("tytul_en"),
                rs.getInt("rok_wydania"),
                rs.getLong("wydawca_id"));
    }

    private static BookDetails mapDetails(ResultSet rs) throws SQLException {
        return new BookDetails(
                rs.getLong("id"),
                rs.getString("autor"),
                rs.getString("tytul_pl"),
                rs.getString("tytul_en"),
                rs.getInt("rok_wydania"),
                rs.getString("wydawca"));
    }
}
