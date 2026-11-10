package pk.wieik.ti.repository;

import pk.wieik.ti.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    public record BookRow(int id, String autor, String tytulPl,
                          String tytulEn, int rokWydania, String wydawca) {}

    public List<BookRow> findAll() throws SQLException {
        String sql = """
            SELECT k.id, a.imie || ' ' || a.nazwisko AS autor,
                   k.tytul_pl, k.tytul_en, k.rok_wydania,
                   w.nazwa AS wydawca
            FROM ksiazki k
            JOIN autorzy a ON a.id = k.autor_id
            JOIN wydawcy w ON w.id = k.wydawca_id
            ORDER BY k.id
            LIMIT 200
            """;
        List<BookRow> rows = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new BookRow(
                    rs.getInt("id"), rs.getString("autor"),
                    rs.getString("tytul_pl"), rs.getString("tytul_en"),
                    rs.getInt("rok_wydania"), rs.getString("wydawca")
                ));
            }
        }
        return rows;
    }
}
